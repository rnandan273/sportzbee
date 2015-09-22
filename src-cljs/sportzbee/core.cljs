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
  (reagent/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
