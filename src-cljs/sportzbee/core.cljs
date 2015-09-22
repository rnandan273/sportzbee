(ns sportzbee.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljsjs.react-bootstrap :as react-bootstrap]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

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


(defn nav-link [uri title page collapsed?]
  [:li {:class (when (= page (session/get :page)) "active")}
   [:a {:href uri
        :on-click #(reset! collapsed? true)}
    title]])
(defn reactnavbar []
  (fn []
    [Navbar {:fixedTop true :brand "Sportzbee" :bsStyle "primary" :bsSize "large" :toggleNavKey 0}
     [CollapsibleNav {:eventKey 0}
     [Nav {:navbar true :eventKey 0}
      [NavItem {:eventKey 1 :href "#/"} "Home"]
      [NavItem {:eventKey 2 :href "#/about"} "About Us"]

      [DropdownButton {:eventKey 3 :title "Sport"}
       [MenuItem {:eventKey "1"} "Table Tennis"]
       [MenuItem {:eventKey "2"} "Chess"]
       [MenuItem {:eventKey "3"} "Badminton"]
       [MenuItem {:eventKey "4"} "Tennis"]
       [MenuItem {:eventKey "5"} "Cricket"]
       [MenuItem {:eventKey "6"} "Soccer"]]
      [DropdownButton {:eventKey 4 :title "Manage Events"}
       [MenuItem {:eventKey "1" :href "#/addevent"} "Add"]
       [MenuItem {:eventKey "2" :href "#/myevents"} "My Events"]]
      ]
      [Nav {:navbar true :right true}
      [NavItem {:eventKey 1 :href "#/login"} "Login"]
      [NavItem {:eventKey 2 :href "#/register"} "Register"]
      ]]

      ]))
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

  (fn [][Carousel {:activeIndex 0 :direction "next"}
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

    )
  )
(defn services []
  (fn []
      [Grid
          [Row
           [Col {:xs 1 :md 3 :sm 1}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Organize"] [:p "Manage events completely"]]

           [Col {:xs 1 :md 3 :sm 1}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Search"] [:p "Search events in your locality"]]
           [Col {:xs 1 :md 3 :sm 1}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Participate"] [:p "Participate and build portfolio"]]
           [Col {:xs 1 :md 3 :sm 1}
                [Thumbnail {:href "#" :alt "171x180" :src "/img/search.png"}]
                [:h3 "Share"] [:p "Share scores and performance with your friends"]]
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
(defn footer []
  [Grid
   [Row
    [Col {:xs 6} [Row [:h3 "Sportzbee"]]
                 [Row [:p "is online social & digital sports platform for sports enthusiast to capture,
                       record and share sports events & matches happening all over the world."]]
                 [Row [:h4 "© 2014 SportzEvents Inc. All rights reserved."]]]
    [Col {:xs 6} [Row [:h5 "Disclaimer"]]
                 [Row [:p "Please contact the organizer of the event that you want to participate or
                       attend. Sportzevents.com attempts to provide information about events and gathers
                       event information from publicly available sources. The information is subject to change.
                       Sportzevents.com does not take responsibility for any loss
                       incurred due to plans made on the basis of the information on the Web site"]
                  ]]]])
(def pages
  {:home #'home-page
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
  (reagent/render [#'services] (.getElementById js/document "services"))
  (reagent/render [#'footer] (.getElementById js/document "footer")))

(defn init! []
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
