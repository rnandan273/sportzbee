(ns sportzbee.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [hickory.core :as hickory]
            [cljs.core.async :as async :refer [chan close!]]
            [cljsjs.react-bootstrap :as react-bootstrap]
            [markdown.core :refer [md->html]]
            [clojure.walk :as walk]
            [cognitect.transit :as t]
            [ajax.core :refer [GET POST]])
  (:import goog.History)
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]))
(def app-state (reagent/atom {:app-about-page-message "This about page message"
                              :background-color "black"
                              :person { :user-name "vijay_yes_retail"
                                       :bank ""
                                       :passwd "secret"
                                       :age 35
                                       :token ""
                                       :action ""
                                       :org_name "rnandan273"
                                       :app_name "mysportzbee"
                                       :email "foo@bar.baz"}
                              :event {:event_name ""
                                      :venue_name ""
                                      :landmark ""
                                      :street ""
                                      :city ""
                                      :state ""
                                      :pin ""
                                      :contact ""
                                      :other_info ""
                                      :start_date ""
                                      :end_date ""
                                      :details ""}
                              :fbpage ""
                              }))


(def Text (reagent/adapt-react-class js/ReactBootstrap.Text))
(def Grid (reagent/adapt-react-class js/ReactBootstrap.Grid))
(def Row (reagent/adapt-react-class js/ReactBootstrap.Row))
(def Col (reagent/adapt-react-class js/ReactBootstrap.Col))
(def Label (reagent/adapt-react-class js/ReactBootstrap.Label))
(def ListGroup (reagent/adapt-react-class js/ReactBootstrap.ListGroup))
(def ListGroupItem (reagent/adapt-react-class js/ReactBootstrap.ListGroupItem))
(def Button (reagent/adapt-react-class js/ReactBootstrap.Button))
(def Label (reagent/adapt-react-class js/ReactBootstrap.Label))
(def Jumbotron (reagent/adapt-react-class js/ReactBootstrap.Jumbotron))
(def Accordion (reagent/adapt-react-class js/ReactBootstrap.Accordion))
(def Panel (reagent/adapt-react-class js/ReactBootstrap.Panel))
(def Modal (reagent/adapt-react-class js/ReactBootstrap.Modal))
(def Navbar (reagent/adapt-react-class js/ReactBootstrap.Navbar))
(def Nav (reagent/adapt-react-class js/ReactBootstrap.Nav))
(def NavItem (reagent/adapt-react-class js/ReactBootstrap.NavItem))
(def DropdownButton (reagent/adapt-react-class js/ReactBootstrap.DropdownButton))
(def MenuItem (reagent/adapt-react-class js/ReactBootstrap.MenuItem))
(def Input (reagent/adapt-react-class js/ReactBootstrap.Input))
(def ButtonInput (reagent/adapt-react-class js/ReactBootstrap.ButtonInput))
(def Thumbnail (reagent/adapt-react-class js/ReactBootstrap.Thumbnail))
(def CollapsibleNav (reagent/adapt-react-class js/ReactBootstrap.CollapsibleNav))
(def Carousel (reagent/adapt-react-class js/ReactBootstrap.Carousel))
(def CarouselItem (reagent/adapt-react-class js/ReactBootstrap.CarouselItem))

(defn log [s]
  (.log js/console (str s)))

(defn response-handler[ch response]
  (go (>! ch response)(close! ch))
  (log "DONE"))

(defn response-handler_test[response]
  (log response))

(defn do-http-get [url]
  (log (str "GET " url))
  (let [ch (chan 1)]
    (GET url {:handler (fn [response](response-handler ch response))
              :error-handler (fn [response] (response-handler ch response))})
    ch))

(defn do-http-post [url doc]
  (log (str "POST " url doc))
  (let [ch (chan 1)]
    (POST url {:params doc :format :raw :handler (fn [response] (response-handler ch response))
               :error-handler (fn [response] (response-handler ch response))})
    ch)
  )

(def read-json (t/reader :json))

(def write-json (t/writer :json-verbose))

(defn forward-login [auth_response]
  (log (str "ERROR ->" (get auth_response :error)))
  (swap! app-state assoc-in [:person :action] (str "Invalid userid / password"))
  (secretary/dispatch! "#/login")
  (session/put! :page :login)
  )

(defn read-server-response [response]
  (walk/keywordize-keys (t/read read-json response)))

(defn read-auth-response [response]
  (swap! app-state assoc-in [:fbpage] (first (map hickory/as-hiccup (hickory/parse-fragment response))))

  (log (str "After Person appstate response "  (@app-state :fbpage)))
  ;;(session/put! :page :fblogin)
  (let [auth_response (read-server-response response)]
    (cond (contains? auth_response :error)
          (forward-login auth_response))

    (cond (contains? auth_response :access_token)
          (
            (swap! app-state assoc-in [:person :token] (str (get auth_response :access_token)))
            (swap! app-state assoc-in [:person :action] (str ""))
            (secretary/dispatch! "/chat")
            (session/put! :page :chat)
            )))
  )

(defn nav-link [uri title page collapsed?]
  [:li {:class (when (= page (session/get :page)) "active")}
   [:a {:href uri
        :on-click #(reset! collapsed? true)}
    title]])

(defn reactnavbar []
  (fn []
    [Navbar {:class "navbar-material-blue-800" :fixedTop true :brand "Sportzbee" :bsStyle "primary" :bsSize "large" :toggleNavKey 0}
     [CollapsibleNav {:eventKey 0}
     [Nav {:navbar true :eventKey 0}
      [NavItem {:class "navitem-material-blue-800" :eventKey 1 :href "#/"} "Home"]
      [NavItem {:class "navitem-material-blue-800" :eventKey 2 :href "#/about"} "About Us"]

      [DropdownButton {:class "dropdownbutton-material-blue-800" :eventKey 3 :title "Sport"}
       [MenuItem {:class "menuitem-material-blue-800" :eventKey "1"} "Table Tennis"]
       [MenuItem {:class "menuitem-material-blue-800" :eventKey "2"} "Chess"]
       [MenuItem {:class "menuitem-material-blue-800" :eventKey "3"} "Badminton"]
       [MenuItem {:class "menuitem-material-blue-800" :eventKey "4"} "Tennis"]
       [MenuItem {:class "menuitem-material-blue-800" :eventKey "5"} "Cricket"]
       [MenuItem {:class "menuitem-material-blue-800" :eventKey "6"} "Soccer"]]

      [DropdownButton {:eventKey 4 :title "Manage Events"}
       [MenuItem {:class "menuitem-material-blue-800":eventKey "1" :href "#/addevent"} "Add"]
       [MenuItem {:class "menuitem-material-blue-800":eventKey "2" :href "#/myevents"} "My Events"]]]

      [Nav {:navbar true :right true}
        [NavItem {:class "navitem-material-blue-800" :eventKey 1 :href "#/login"} "Login"]
        [NavItem {:class "navitem-material-blue-800" :eventKey 2 :href "#/register"} "Register"]]]]))

(defn navbar []
  (let [collapsed? (atom true)]
    (fn []
      [:nav.navbar.navbar-inverse.navbar-fixed-top
       [:div.container
        [:div.navbar-header
         [:button.navbar-toggle
          {:class         (when-not @collapsed? "collapsed")
           :data-toggle   "collapse"
           :aria-expanded @collapsed?
           :aria-controls "navbar"
           :on-click      #(swap! collapsed? not)}
          [:span.sr-only "Toggle Navigation"]
          [:span.icon-bar]
          [:span.icon-bar]
          [:span.icon-bar]]
         [:a.navbar-brand {:href "#/"} "Sportzbee"]]
        [:div.navbar-collapse.collapse
         (when-not @collapsed? {:class "in"})
         [:ul.nav.navbar-nav
          [nav-link "#/" "Home" :home collapsed?]
          [nav-link "#/about" "About" :about collapsed?]]]]])))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "this is the story of sportzbee... work in progress"]]])

(defn home-page []
  (fn []
    [:div.container
     [:div [Carousel {:activeIndex 0 :direction "next"}
        [CarouselItem
          [:img {:width 600 :height 250 :alt "600x250" :src "/img/participate.jpeg"}]
          [:div {:className "carousel-caption"}
            [:h3 "Organize Tournaments and Manage them completely"]
            [:p [:a.btn.btn-primary.btn-lg "Learn more »"]]]]
        [CarouselItem
          [:img {:width 600 :height 250 :alt "600x250" :src "/img/list.jpeg"}]
          [:div {:className "carousel-caption"}
            [:h3 "Search and participate in your favourite Sport events"]
            [:p "Nulla vitae elit libero, a pharetra augue mollis interdum"]]]
        [CarouselItem
          [:img {:width 600 :height 250 :alt "600x250" :src "/img/record.jpeg"}]
          [:div {:className "carousel-caption"}
            [:h3 "Record Scores and build portfolio, share with friends"]
            [:p "Nulla vitae elit libero, a pharetra augue mollis interdum"]]]
        [CarouselItem
          [:img {:width 600 :height 250 :alt "600x250" :src "/img/capture.jpeg"}]
          [:div {:className "carousel-caption"}
            [:h3 "Share sport events with your friends"]
            [:p "Nulla vitae elit libero, a pharetra augue mollis interdum"]]]
      ]
      ]
     [:br][:br]
     [:div
      [Grid {:fluid true}
          [Row
           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Organize"] [:p "Manage events completely"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]

           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Search"] [:p "Search events in your locality"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]
           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Participate"] [:p "Participate and build portfolio"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]
           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Share"] [:p "Share scores and performance with your friends"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]
               ]
     ]]]

    )
  )
(defn services []
  (fn []
      [Grid {:fluid true}
          [Row
           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Organize"] [:p "Manage events completely"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]

           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Search"] [:p "Search events in your locality"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]

           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Participate"] [:p "Participate and build portfolio"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]

           [Col {:xs 12 :md 3 :sm 4}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Share"] [:p "Share scores with your friends"]
                [:p [Button {:class "btn-material-blue-800" :bsStyle "primary"}"More"]]]
               ]
     ]

 ))

(defn home-page1 []
  [:div.container
   [:div.jumbotron
    [:h1 "Welcome to sportzbee"]
    [:p "Time to start building your site!"]
    [:p [:a.btn.btn-primary.btn-lg {:href "http://luminusweb.net"} "Learn more »"]]]
   [:div.row
    [:div.col-md-12
     [:h2 "Welcome to ClojureScript"]]]
   (when-let [docs (session/get :docs)]
     [:div.row
      [:div.col-md-12
       [:div {:dangerouslySetInnerHTML
              {:__html (md->html docs)}}]]])])

(defn my-events []
  [:div.container
   [:div.row
    [:div.col-md-12
     "List of my events"]]])

(defn login-page []
  (let [login_doc (reagent/atom (@app-state :person) :many {:options :foo})]
    (fn []
      [:form  {:className "form-horizontal"}
       [Grid
        [Row [Col {:mdOffset 3 :md 9 :xsOffset 2 :xs 10 }[:h2 "Welcome , Login"]]]

        [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                :type "text" :bsSize "small" :label "Username" :placeholder "Enter text"
                :onChange #(swap! login_doc assoc-in [:user-name] (-> % .-target .-value))}]

        [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                :type "password" :bsSize "small" :label "Password" :placeholder "Enter password"
                :onChange #(swap! login_doc assoc-in [:passwd] (-> % .-target .-value))}]

        [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                :type "email" :bsSize "small" :label "Email Address" :placeholder "Enter email"}]
        [Row
         [Col {:mdOffset 3 :md 3 :xsOffset 2 :xs 4 }
          [ButtonInput {:class "btn-material-blue-800" :type "reset" :bsStyle "primary" :value "Reset"}]
          ]
         [Col {:md 3 :xs 4 }
          [ButtonInput {:class "btn-material-blue-800" :type "submit" :bsStyle "primary" :value "Login" :onClick #(user-login-click @login_doc)}]
          ]
        ]]])))

(def url_list {:usertoken1
                 (fn [username password]
                   (str "/api/getUserToken?grant_type=password&username=" username "&password=" password))
               :usertoken
                 (fn [username password]
                   (str "/usertoken?grant_type=password&username=" username "&password=" password))
               :user_register
                 (fn [username passwd rt_passwd email]
                    (str "/register"))})


(defn user-login-click [{:keys [user-name passwd]} doc]
  (log (str "User Login destructuring" user-name passwd))
  (go
    (read-auth-response (<! (do-http-get ((get url_list :usertoken) user-name passwd))))))

(defn user-fb-login-click []

  (def usergrid_url (str "https://graph.facebook.com/oauth/authorize?client_id=116170012066426&scope=email&response_type=code&redirect_uri=http://localhost:3000/api/fb_callback"))
  (def usergrid_url (str "/fblogin"))
  ;;(def usergrid_url (str "https://www.facebook.com/dialog/oauth?client_id=116170012066426&scope=email&response_type=code&redirect_uri=http://localhost:3000/api/fb_callback"))
  (log (str "FB Login" usergrid_url))
  (do-http-get usergrid_url)
  (comment
  (go
     (read-auth-response (<! (do-http-get usergrid_url))))
  ))

(defn user-register-click [doc]
  (let [{:keys [user-name passwd rt_passwd email]} doc]
  (def post_request (.stringify js/JSON (clj->js doc)))
    (log (str "POST REGISTER 1" doc))
  (go
     (read-auth-response (<! (do-http-post ((get url_list :user_register) username passwd rt_passwd email) doc))))))


(defn register-page []
  (let [register_doc (reagent/atom (@app-state :person) :many {:options :foo})]
    (fn []
      [:form  {:className "form-horizontal"}
       [Grid
        [Row [Col {:mdOffset 3 :md 9 :xsOffset 2 :xs 10 }[:h2 "Register New User"]]]
        [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                :type "text" :bsSize "small" :label "Username" :placeholder "Enter text"
                :onChange #(swap! register_doc assoc-in [:user_name] (-> % .-target .-value))}]

        [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                :type "password" :bsSize "small" :label "Password" :placeholder "Enter password"
                :onChange #(swap! register_doc assoc-in [:passwd] (-> % .-target .-value))}]

        [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                :type "password" :bsSize "small" :label "Password" :placeholder "Retype password"
                :onChange #(swap! register_doc assoc-in [:retype_passwd] (-> % .-target .-value))}]

        [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                :type "email" :bsSize "small" :label "Email Address" :placeholder "Enter email"
                :onChange #(swap! register_doc assoc-in [:email] (-> % .-target .-value))}]
        [Row
         [Col {:mdOffset 3 :md 3 :xsOffset 2 :xs 4 }
          [ButtonInput {:type "reset" :class "btn-material-blue-800" :bsStyle "primary" :value "Reset"}]]
         [Col {:md 3 :xs 4 }
          [ButtonInput {:type "submit" :class "btn-material-blue-800" :bsStyle "primary" :value "Register" :onClick #(user-register-click @register_doc)}]]]
       ]])))

(defn register-event []
  (let [event_doc (reagent/atom (@app-state :event))]
    (fn []
      [:form  {:className "form-horizontal"}
        [Grid
          [Row [Col {:mdOffset 3 :md 9 :xsOffset 2 :xs 10 }[:h2 "Register New Event"]]]
          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "Event Name" :placeholder "Enter event name"
                  :onChange #(swap! event_doc assoc-in [:event_name] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "Venue Name" :placeholder "Enter venue name"
                  :onChange #(swap! event_doc assoc-in [:venue_name] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "Street Name":placeholder "Enter :street name"
                  :onChange #(swap! event_doc assoc-in [:street] (-> % .-target .-value))}]

           [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                 :type "text" :bsSize "small" :label "Landmark" :placeholder "Enter landmark name"
                  :onChange #(swap! event_doc assoc-in [:landmark] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "City" :placeholder "Enter city"
                  :onChange #(swap! event_doc assoc-in [:city] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "State" :placeholder "Enter state"
                  :onChange #(swap! event_doc assoc-in [:state] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "PIN" :placeholder "Enter pin code"
                  :onChange #(swap! event_doc assoc-in [:pin] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "Contact info" :placeholder "Enter contact into, cell, home or office"
                  :onChange #(swap! event_doc assoc-in [:contact_info] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "text" :bsSize "small" :label "Other info" :placeholder "Any other info"
                  :onChange #(swap! event_doc assoc-in [:other_info] (-> % .-target .-value))}]

          [Input {:mdOffset 4 :xsOffset 2 :labelClassName "col-xs-2" :wrapperClassName "col-xs-6"
                  :type "textarea" :bsSize "small" :label "Details" :placeholder "Details"
                  :onChange #(swap! event_doc assoc-in [:details] (-> % .-target .-value))}]

       [Row
        [Col {:mdOffset 3 :md 3 :xsOffset 2 :xs 4 }
          [ButtonInput {:type "reset" :class "btn-material-blue-800" :bsStyle "primary" :value "Reset"}]]
        [Col {:md 3 :xs 4 }
          [ButtonInput {:type "submit" :class "btn-material-blue-800" :bsStyle "primary" :value "Register"
                        :onClick #(user-register-click @register_doc)}]]]]]

      )))

(defn search_entry [entry]
  (log (str "Search : " entry)))

(defn footer []
  [Grid
   [Row
    [Col {:mdOffset 1 :xsOffset 1 :xs 5 :md 5} [Row [:h3 "Sportzbee"]]
                 [Row [:p "is online social & digital sports platform for sports enthusiast to capture,
                       record and share sports events & matches happening all over the world."]]
                 [Row [:h4 "© 2014 SportzEvents Inc. All rights reserved."]]]
    [Col {:xs 5 :md 5} [Row [:h5 "Disclaimer"]]
                 [Row [:p "Please contact the organizer of the event that you want to participate or
                       attend. Sportzevents.com attempts to provide information about events and gathers
                       event information from publicly available sources. The information is subject to change.
                       Sportzevents.com does not take responsibility for any loss
                       incurred due to plans made on the basis of the information on the Web site"]
                  ]]]])

(defn fblogin-page []
  (@app-state :fbpage))

(def pages
  {:home #'home-page
   :register #'register-page
   :addevent #'register-event
   :myevents #'my-events
   :login #'login-page
   :fblogin #'fblogin-page
   :about #'about-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

(secretary/defroute "/register" []
  (session/put! :page :register))

(secretary/defroute "/addevent" []
  (session/put! :page :addevent))

(secretary/defroute "/myevents" []
  (session/put! :page :myevents))

(secretary/defroute "/chat" []
  (session/put! :page :chat))

(secretary/defroute "/login" []
  (session/put! :page :login))

(secretary/defroute "/logout" []
  (session/put! :page :logout))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          EventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET (str js/context "/docs") {:handler #(session/put! :docs %)}))

(defn mount-components []
  ;;(reagent/render [#'navbar] (.getElementById js/document "navbar"))
  (reagent/render [#'reactnavbar] (.getElementById js/document "reactnavbar"))
  (reagent/render [#'page] (.getElementById js/document "app"))
  ;;(reagent/render [#'services] (.getElementById js/document "services"))
  (reagent/render [#'footer] (.getElementById js/document "footer")))

(defn init! []
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
