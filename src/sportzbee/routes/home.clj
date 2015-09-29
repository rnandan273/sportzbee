(ns sportzbee.routes.home
  (:require [sportzbee.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [sportzbee.datomic_utils :as dq]
            [sportzbee.sportzbee_db_utils :as sbu]
            [sportzbee.usergrid_utils :as ug]
            [sportzbee.oauth_utils :as oa]
            [ring.util.http-response :refer [ok found]]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" [] (home-page))

  (GET "/dbcmd" req (cond (= (:op (:params req)) "setup") (ok (dq/dbsetup))
                          (= (:op (:params req)) "list") (ok (dq/dblist))
                          (= (:op (:params req)) "clean") (ok (dq/dbshutdown))
                          (= (:op (:params req)) "load") (ok (dq/dbload))
                          (= (:op (:params req)) "seed") (ok (dq/dbseed))
                          (= (:op (:params req)) "query") (ok (dq/dbquery))))

  (GET "/usertoken" req
       (timbre/info "GET PARAMS1 " (:username (:params req)) (:password (:params req)) (:email (:params req)))
       (ok (ug/get-user-token  (:grant_type (:params req)) (:username (:params req)) (:password (:params req)))))

  (POST "/register" req (println "Do something")
        (ok(ug/register_user req)))

  (GET "/syncfb" [] (timbre/info "In SYNC FB 123 :" )
        (found(oa/get_fb_url)))

  (GET "/fb_callback" req
       (timbre/info "In FB Callback :" (:code (:params req)))
       (oa/fb-handler req))

  (GET "/logout" []
       (oa/logout))


  (GET "/docs" [] (ok (-> "docs/docs.md" io/resource slurp)))

  ;; sportzbee related
  (GET "/sbcmd" req (cond (= (:op (:params req)) "setup") (ok (sbu/dbsetup))
                          (= (:op (:params req)) "list") (ok (sbu/dblist))
                          (= (:op (:params req)) "clean") (ok (sbu/dbshutdown))
                          (= (:op (:params req)) "load") (ok (sbu/dbload))
                          (= (:op (:params req)) "query") (ok (sbu/dbquery))
                          (= (:op (:params req)) "seed") (ok (sbu/dbseed))))
  (GET "/listed_events" []
       (ok (sbu/get_tourneys)))

  )



