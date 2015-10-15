(ns sportzbee.routes.home
  (:require [sportzbee.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [sportzbee.datomic_utils :as dq]
            [sportzbee.ml_utils :as ml]
            [sportzbee.sportzbee_db_utils :as sbu]
            [sportzbee.usergrid_utils :as ug]
            [sportzbee.oauth_utils :as oa]
            [ring.util.http-response :refer [ok found]]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]
            [clojure.set :refer [union]]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! alt!! timeout]]))

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
       (ml/test_config)
       (ok (sbu/get_tourneys)))

  (GET "/getty_images" []
       (ok (sbu/get_images)))


  (GET "/search" [] (ok (<!! (sbu/async_query))))

  (GET "/multisearch" []

    (let [ch1 (chan)
          ch2 (chan)
          ch3 (chan)]

    (sbu/fetch_images ch1)

    (sbu/fetch_images ch2)

    (sbu/fetch_images ch3)

      (let [[query_response channel] (alts!! [ch1 ch2 ch3])]
       (timbre/info "Response from ALTS!!!")
       (ok query_response))))


  (GET "/randomsearch" req

    (let [ch1 (chan)
          ch2 (chan)
          ch3 (chan)
          fp (:fp (:params req))]
      (timbre/info "filter is " fp)

    (sbu/search_events_by_sport ch1 fp)

    (sbu/search_events_by_city ch2 fp)

    (sbu/search_events_by_pin ch3 fp)

      (let [res1 (into #{} (<!! ch1))
            res2 (into #{} (<!! ch2))
            res3 (into #{} (<!! ch3))]
      (ok (into [] (union  res1 res2 res3))))
     )

  )
  )



