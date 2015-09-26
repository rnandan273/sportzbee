(ns sportzbee.oauth_utils
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [ring.util.http-response :refer :all]
            [ring.util.response :as response]
            [cemerick.friend :as friend]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [taoensso.timbre :as timbre]))

(def APP_ID "116170012066426")

(def APP_SECRET "22482ed438d84c5155990631f11a20f0")

(def REDIRECT_URI "http://localhost:3000/fb_callback")

(def FB_ACCESS_TOKEN_URL "https://graph.facebook.com/oauth/access_token")

(defn get_fb_url []
  (str "https://graph.facebook.com/oauth/authorize?client_id=" APP_ID "&scope=email&response_type=code&redirect_uri=" REDIRECT_URI))


(defn get_fb_access_token [response]
  (timbre/info "FB CODE : " response)
  (timbre/info (get (str/split (get (str/split response #"=") 1) #"&expires") 0))
  (timbre/info (-> (str/split response #"=")
      (get 1)
      (str/split #"&expires")
      (get 0)
      ))

  (-> (str/split response #"=")
      (get 1)
      (str/split #"&expires")
      (get 0))
)
(defn access_fb_data [body]
   (def fb_access_url (str "https://api.usergrid.com/catamera/yes_retail_ebanking_app/auth/facebook?fb_access_token=" (get_fb_access_token body)))
   (timbre/info "FB url->" fb_access_url)
   (let [{:keys [status headers body error] :as resp} @(http/get fb_access_url)]
     (if error
       (timbre/info  "Failed, exception: " error)
       (json/write-str (json/read-str body :key-fn keyword)))))

(defn send-redirect-resp [msg]
  {:status 302
     :headers {"location" "/#/"
               "set-cookie" (str "token=" "RAGHU" ";Path=/")}}
)

(defn fb-handler [req]

    (def fb_access_token_url (str FB_ACCESS_TOKEN_URL "?client_id=" APP_ID "&client_secret=" APP_SECRET "&code=" (:code (:params req)) "&redirect_uri=" REDIRECT_URI))
      (timbre/info "FB access token url->" fb_access_token_url)

      (let [{:keys [status headers body error] :as resp} @(http/get fb_access_token_url)]
        (if error
          (timbre/info  "Failed, exception: " error)
          (do
          (access_fb_data body)
          (send-redirect-resp (json/write-str {:name "xxx"}))
           )

          )
    )
)
