(ns sportzbee.usergrid_queries
  (:require [ring.util.http-response :refer [ok]]
            [ring.util.http-response :refer :all]
            [ring.util.response :as response]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [taoensso.timbre :as timbre]))

(def url_list
      {:usertoken
       (fn [username password]
        (str "http://api.usergrid.com/rnandan273/mysportzbee/token?grant_type=password&username=" username "&password=" password))
       :admintoken (fn [username password]
        (str "http://api.usergrid.com/management/token?grant_type=password&username=" username "&password=" password))
       :users (fn [org_name app_name]
        (str "http://api.usergrid.com/" org_name "/" app_name "/users"))
       })

(defn get-user-token [grant_type username password]
  (timbre/info  "Reached Server getting admintoken: " username password)
  (def options {:headers {"Content-Type" "application/json"}})
   (let [{:keys [status headers body error] :as resp} @(http/get ((:usertoken url_list) username password))]
    (if error
      (timbre/info  "Failed, exception: " error)
      (json/write-str (json/read-str body :key-fn keyword)))))


(defn get-admin-token [username password]
  (timbre/info  "Reached Server getting admintoken: " username password)
  (def options {:headers {"Content-Type" "application/json"}})
   (let [{:keys [status headers body error] :as resp} @(http/get ((:admintoken url_list) username password))]
    (if error
      (timbre/info  "Failed, exception: " error)
      (json/read-str body :key-fn keyword))))

(def app_config "appconfig")

(defn create-user-usergrid [admin_token body ug_url app_config]
  (timbre/info "Admin token " admin_token body  ug_url app_config)
  (def new_user {
          :username (:user_name body)
          :name (:user_name body)
          :firstname (:user_name body)
          :lastname (:user_name body)
          :designation "Customer"
          :email (:email body)
          :password (:password body)
          })

  (def new_user_json (json/write-str new_user))
  (timbre/info "Creating new User" new_user_json)
  (let [options {:body new_user_json :headers {"Content-Type" "application/json" "Authorization" (str "Bearer " admin_token)}}
      {:keys [status headers body error]} @(http/post ug_url options)]
  (if error
    (println "Failed, exception is " error)
    (json/write-str (json/read-str body :key-fn keyword))))
)

(defn create-user [body]
  (timbre/info "Creating user " body)
  (let [adminresp (get-admin-token "raghu.sk@gmail.com" "Mig27372")]
    (timbre/info  "ADMIN RESP " adminresp)
    (if (contains? adminresp :access_token)
          (let [admin_token (get-in adminresp [:access_token])
                org_name (:org_name body)
                app_name (:app_name body)]
            (create-user-usergrid admin_token body ((:users url_list) org_name app_name) app_config)
          )
          (timbre/info "Error token "))))

(defn register_user [request]
  (let [user_data (or (get-in request [:params])
                       (get-in request [:body]))]
    (timbre/info (str "You posted: " user_data))
    (create-user user_data)))

