(defproject sportzbee "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [selmer "0.9.2"]
                 [com.taoensso/timbre "4.1.1"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.74"]
                 [environ "1.0.1"]
                 [compojure "1.4.0"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.1.5"]
                 [ring "1.4.0"
                  :exclusions [ring/ring-jetty-adapter]]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.3"]
                 [bouncer "0.3.3"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.11"]
                 [org.webjars/bootstrap "3.3.5"]
                 [org.webjars/jquery "2.1.4"]
                 [org.clojure/clojurescript "1.7.107" :scope "provided"]
                 [cljsjs/react-bootstrap "0.23.7-0" :exclusions [org.webjars.bower/jquery]]
                 [org.clojure/tools.reader "0.9.2"]
                 [reagent "0.5.0" :exclusions [cljsjs/react]]
                 [reagent-forms "0.5.9"]
                 [reagent-utils "0.1.5"]
                 [secretary "1.2.3"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-ajax "0.3.14"]
                 [hickory "0.5.4"]
                 [http-kit "2.1.19"]
                 [org.immutant/web "2.1.0"]
                 [com.cognitect/transit-cljs "0.8.205"]
                 [org.clojure/core.memoize "0.5.6"]
                 [com.datomic/datomic-free "0.9.5153"  :exclusions [joda-time]]
                 [org.clojure/core.logic "0.8.5"]
                 [com.cemerick/friend "0.2.0"]
                 [org.clojure/data.json "0.2.6"]
                 [cljsjs/google-maps "3.18-1"]
                 [bk/ring-gzip "0.1.1"]]

  :min-lein-version "2.0.0"
  :uberjar-name "sportzbee.jar"
  :jvm-opts ["-server"]

  :main sportzbee.core

  :plugins [[lein-environ "1.0.1"]
            [lein-cljsbuild "1.1.0"]]
  :clean-targets ^{:protect false} [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :cljsbuild
  {:builds
   {:app
    {:source-paths ["src-cljs"]
     :compiler
     {:output-to "resources/public/js/app.js"
      :externs ["react/externs/react.js" "externs.js"]
      :foreign-libs [{:file ~(str "https://maps.googleapis.com/maps/api/js?key=AIzaSyDUexZHH88EIeKZSS6U-efg0KDMQCZoH3w&libraries=places")
                            :provides ["google.maps"]}]
      :pretty-print true}}}}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
              :hooks [leiningen.cljsbuild]
              :cljsbuild
              {:jar true
               :builds
               {:app
                {:source-paths ["env/prod/cljs"]
                 :compiler {:optimizations :advanced :pretty-print false}}}}

             :aot :all}
   :dev           [:project/dev :profiles/dev]
   :test          [:project/test :profiles/test]
   :project/dev  {:dependencies [[ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.4.0"]
                                 [pjstadig/humane-test-output "0.7.0"]
                                 [lein-figwheel "0.4.0"]]
                  :plugins [[lein-figwheel "0.4.0"] [hiccup-bridge "1.0.1"]]
                   :cljsbuild
                   {:builds
                    {:app
                     {:source-paths ["env/dev/cljs"] :compiler {:source-map true}}}}

                  :figwheel
                  {:http-server-root "public"
                   :server-port 3449
                   :nrepl-port 7002
                   :css-dirs ["resources/public/css"]
                   :ring-handler sportzbee.handler/app}

                  :repl-options {:init-ns sportzbee.core}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]
                  ;;when :nrepl-port is set the application starts the nREPL server on load
                  :env {:dev        true
                        :port       3000
                        :nrepl-port 7000}}
   :project/test {:env {:test       true
                        :port       3001
                        :nrepl-port 7001}}
   :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo" :creds :gpg}}
   :profiles/dev {}
   :profiles/test {}})
