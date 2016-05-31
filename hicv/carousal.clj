("<!DOCTYPE html>"
 "<!-- saved from url=(0042)http://getbootstrap.com/examples/carousel/ -->"
 [:html
  {:lang "en"}
  [:head
   [:meta
    {:http-equiv "Content-Type", :content "text/html; charset=UTF-8"}]
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible", :content "IE=edge"}]
   [:meta
    {:name "viewport", :content "width=device-width, initial-scale=1"}]
   "<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->"
   [:meta {:name "description", :content ""}]
   [:meta {:name "author", :content ""}]
   [:link {:rel "icon", :href "http://getbootstrap.com/favicon.ico"}]
   [:title "Carousel Template for Bootstrap"]
   "<!-- Bootstrap core CSS -->"
   [:link
    {:href "./Carousel Template for Bootstrap_files/bootstrap.min.css",
     :rel "stylesheet"}]
   "<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->"
   "<!--[if lt IE 9]><script src=\"../../assets/js/ie8-responsive-file-warning.js\"></script><![endif]-->"
   [:script
    {:src
     "./Carousel Template for Bootstrap_files/ie-emulation-modes-warning.js"}]
   "<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->"
   "<!--[if lt IE 9]>\n      <script src=\"https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js\"></script>\n      <script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\n    <![endif]-->"
   "<!-- Custom styles for this template -->"
   [:link
    {:href "./Carousel Template for Bootstrap_files/carousel.css",
     :rel "stylesheet"}]]
  "<!-- NAVBAR\n================================================== -->"
  [:body
   [:div.navbar-wrapper
    [:div.container
     [:nav.navbar.navbar-inverse.navbar-static-top
      [:div.container
       [:div.navbar-header
        [:button.navbar-toggle.collapsed
         {:type "button",
          :data-toggle "collapse",
          :data-target "#navbar",
          :aria-expanded "false",
          :aria-controls "navbar"}
         [:span.sr-only "Toggle navigation"]
         [:span.icon-bar]
         [:span.icon-bar]
         [:span.icon-bar]]
        [:a.navbar-brand
         {:href "http://getbootstrap.com/examples/carousel/#"}
         "Project name"]]
       [:div#navbar.navbar-collapse.collapse
        [:ul.nav.navbar-nav
         [:li.active
          [:a
           {:href "http://getbootstrap.com/examples/carousel/#"}
           "Home"]]
         [:li
          [:a
           {:href "http://getbootstrap.com/examples/carousel/#about"}
           "About"]]
         [:li
          [:a
           {:href "http://getbootstrap.com/examples/carousel/#contact"}
           "Contact"]]
         [:li.dropdown
          [:a.dropdown-toggle
           {:href "http://getbootstrap.com/examples/carousel/#",
            :data-toggle "dropdown",
            :role "button",
            :aria-haspopup "true",
            :aria-expanded "false"}
           "Dropdown "
           [:span.caret]]
          [:ul.dropdown-menu
           [:li
            [:a
             {:href "http://getbootstrap.com/examples/carousel/#"}
             "Action"]]
           [:li
            [:a
             {:href "http://getbootstrap.com/examples/carousel/#"}
             "Another action"]]
           [:li
            [:a
             {:href "http://getbootstrap.com/examples/carousel/#"}
             "Something else here"]]
           [:li.divider {:role "separator"}]
           [:li.dropdown-header "Nav header"]
           [:li
            [:a
             {:href "http://getbootstrap.com/examples/carousel/#"}
             "Separated link"]]
           [:li
            [:a
             {:href "http://getbootstrap.com/examples/carousel/#"}
             "One more separated link"]]]]]]]]]]
   "<!-- Carousel\n    ================================================== -->"
   [:div#myCarousel.carousel.slide
    {:data-ride "carousel"}
    "<!-- Indicators -->"
    [:ol.carousel-indicators
     [:li.active {:data-target "#myCarousel", :data-slide-to "0"}]
     [:li {:data-target "#myCarousel", :data-slide-to "1"}]
     [:li {:data-target "#myCarousel", :data-slide-to "2"}]]
    [:div.carousel-inner
     {:role "listbox"}
     [:div.item.active
      [:img.first-slide
       {:src
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==",
        :alt "First slide"}]
      [:div.container
       [:div.carousel-caption
        [:h1 "Example headline."]
        [:p
         "Note: If you're viewing this page via a "
         [:code "file://"]
         " URL, the \"next\" and \"previous\" Glyphicon buttons on the left and right might not load/display properly due to web browser security rules."]
        [:p
         [:a.btn.btn-lg.btn-primary
          {:href "http://getbootstrap.com/examples/carousel/#",
           :role "button"}
          "Sign up today"]]]]]
     [:div.item
      [:img.second-slide
       {:src
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==",
        :alt "Second slide"}]
      [:div.container
       [:div.carousel-caption
        [:h1 "Another example headline."]
        [:p
         "Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit."]
        [:p
         [:a.btn.btn-lg.btn-primary
          {:href "http://getbootstrap.com/examples/carousel/#",
           :role "button"}
          "Learn more"]]]]]
     [:div.item
      [:img.third-slide
       {:src
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==",
        :alt "Third slide"}]
      [:div.container
       [:div.carousel-caption
        [:h1 "One more for good measure."]
        [:p
         "Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit."]
        [:p
         [:a.btn.btn-lg.btn-primary
          {:href "http://getbootstrap.com/examples/carousel/#",
           :role "button"}
          "Browse gallery"]]]]]]
    [:a.left.carousel-control
     {:href "http://getbootstrap.com/examples/carousel/#myCarousel",
      :role "button",
      :data-slide "prev"}
     [:span.glyphicon.glyphicon-chevron-left {:aria-hidden "true"}]
     [:span.sr-only "Previous"]]
    [:a.right.carousel-control
     {:href "http://getbootstrap.com/examples/carousel/#myCarousel",
      :role "button",
      :data-slide "next"}
     [:span.glyphicon.glyphicon-chevron-right {:aria-hidden "true"}]
     [:span.sr-only "Next"]]]
   "<!-- /.carousel -->"
   "<!-- Marketing messaging and featurettes\n    ================================================== -->"
   "<!-- Wrap the rest of the page in another container to center all the content. -->"
   [:div.container.marketing
    "<!-- Three columns of text below the carousel -->"
    [:div.row
     [:div.col-lg-4
      [:img.img-circle
       {:src
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==",
        :alt "Generic placeholder image",
        :width "140",
        :height "140"}]
      [:h2 "Heading"]
      [:p
       "Donec sed odio dui. Etiam porta sem malesuada magna mollis euismod. Nullam id dolor id nibh ultricies vehicula ut id elit. Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Praesent commodo cursus magna."]
      [:p
       [:a.btn.btn-default
        {:href "http://getbootstrap.com/examples/carousel/#",
         :role "button"}
        "View details »"]]]
     "<!-- /.col-lg-4 -->"
     [:div.col-lg-4
      [:img.img-circle
       {:src
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==",
        :alt "Generic placeholder image",
        :width "140",
        :height "140"}]
      [:h2 "Heading"]
      [:p
       "Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Cras mattis consectetur purus sit amet fermentum. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh."]
      [:p
       [:a.btn.btn-default
        {:href "http://getbootstrap.com/examples/carousel/#",
         :role "button"}
        "View details »"]]]
     "<!-- /.col-lg-4 -->"
     [:div.col-lg-4
      [:img.img-circle
       {:src
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==",
        :alt "Generic placeholder image",
        :width "140",
        :height "140"}]
      [:h2 "Heading"]
      [:p
       "Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus."]
      [:p
       [:a.btn.btn-default
        {:href "http://getbootstrap.com/examples/carousel/#",
         :role "button"}
        "View details »"]]]
     "<!-- /.col-lg-4 -->"]
    "<!-- /.row -->"
    "<!-- START THE FEATURETTES -->"
    [:hr.featurette-divider]
    [:div.row.featurette
     [:div.col-md-7
      [:h2.featurette-heading
       "First featurette heading. "
       [:span.text-muted "It'll blow your mind."]]
      [:p.lead
       "Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo."]]
     [:div.col-md-5
      [:img.featurette-image.img-responsive.center-block
       {:data-src "holder.js/500x500/auto",
        :alt "Generic placeholder image"}]]]
    [:hr.featurette-divider]
    [:div.row.featurette
     [:div.col-md-7.col-md-push-5
      [:h2.featurette-heading
       "Oh yeah, it's that good. "
       [:span.text-muted "See for yourself."]]
      [:p.lead
       "Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo."]]
     [:div.col-md-5.col-md-pull-7
      [:img.featurette-image.img-responsive.center-block
       {:data-src "holder.js/500x500/auto",
        :alt "Generic placeholder image"}]]]
    [:hr.featurette-divider]
    [:div.row.featurette
     [:div.col-md-7
      [:h2.featurette-heading
       "And lastly, this one. "
       [:span.text-muted "Checkmate."]]
      [:p.lead
       "Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo."]]
     [:div.col-md-5
      [:img.featurette-image.img-responsive.center-block
       {:data-src "holder.js/500x500/auto",
        :alt "Generic placeholder image"}]]]
    [:hr.featurette-divider]
    "<!-- /END THE FEATURETTES -->"
    "<!-- FOOTER -->"
    [:footer
     [:p.pull-right
      [:a
       {:href "http://getbootstrap.com/examples/carousel/#"}
       "Back to top"]]
     [:p
      "© 2014 Company, Inc. · "
      [:a
       {:href "http://getbootstrap.com/examples/carousel/#"}
       "Privacy"]
      " · "
      [:a
       {:href "http://getbootstrap.com/examples/carousel/#"}
       "Terms"]]]]
   "<!-- /.container -->"
   "<!-- Bootstrap core JavaScript\n    ================================================== -->"
   "<!-- Placed at the end of the document so the pages load faster -->"
   [:script
    {:src "./Carousel Template for Bootstrap_files/jquery.min.js"}]
   [:script
    {:src "./Carousel Template for Bootstrap_files/bootstrap.min.js"}]
   "<!-- Just to make our placeholder images work. Don't actually copy the next line! -->"
   [:script
    {:src "./Carousel Template for Bootstrap_files/holder.min.js"}]
   "<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->"
   [:script
    {:src
     "./Carousel Template for Bootstrap_files/ie10-viewport-bug-workaround.js"}]]])
