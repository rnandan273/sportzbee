(ns sportzbee.routes.home
  (:require [sportzbee.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [sportzbee.datomic_queries :as dq]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/dbsetup" [] (ok (dq/dbsetup)))
  (GET "/dbshutdown" [] (ok (dq/dbshutdown)))
  (GET "/dblist" [] (ok (dq/dblist)))
  (GET "/dbload" [] (ok (dq/dbload)))
  (GET "/dbseed" [] (ok (dq/dbseed)))
  (GET "/dbquery" [] (ok (dq/dbquery)))
  (GET "/docs" [] (ok (-> "docs/docs.md" io/resource slurp))))

