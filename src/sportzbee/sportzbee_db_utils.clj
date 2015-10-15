(ns sportzbee.sportzbee_db_utils
  (:require [clojure.data.json :as json])
  (:require [taoensso.timbre :as timbre])
  (:require [sportzbee.sportzbee_engine :as reng])
  (:require [datomic.api :as d])
  (:require [datomic.api :only [q db] :as d])
  (:require [org.httpkit.client :as http])
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]])
  (:use [clojure.pprint :as pprint])
)

;;(def uri "datomic:free://localhost:4334/sportzbee")
(def uri "datomic:mem://sportzbee-db")
;;
(defn listdbs []
 ;; (timbre/info "EXISTING dbs" (d/get-database-names "datomic:free://localhost:4334/*")))
  (timbre/info "EXISTING dbs" (d/get-database-names "datomic:mem://*")))

(defn load-schema [dburi]
  (let [conn (d/connect dburi)
        schema (load-file "resources/datomic/sb_schema.edn")]
      (d/transact conn schema)
      conn))

(defn setup-db [dburi]
  (timbre/info "CREATING db " (d/create-database dburi)))

(defn load-data [dburi]
  (load-schema dburi))

(defn shutdown-db [dburi]
  (timbre/info "DELETING db " (d/delete-database dburi)))

(defn dbsetup []
  (listdbs)
  (timbre/info "CREATE DB")
  (setup-db uri)
  (listdbs))

(defn dbload []
  (timbre/info "LOAD DB")
  (load-data uri)
  (listdbs))

(defn dbshutdown []
  (listdbs)
  (timbre/info "SHUTDOWN DB")
  (shutdown-db uri)
  (listdbs))

(defn dblist []
  (listdbs))

(def persons (map #(zipmap [:person-name :age :address :phone :email :morf :sports :likes] %) [["raghu" 42 "Iskcon Gokulam Bangalore" "9886615961" "raghu@gmail.com" "male" "cricket" "football"]
                                                                                ["anand" 42 "Aundh Pune" "9845272298" "anand@gmail.com" "male" "cricket" "tennis"]
                                                                                ["ramesh" 43 "Basaveshwarnagar Bangalore" "9886615962" "ramesh@gmail.com" "male" "cricket" "tt"]
                                                                                ["prakash" 42 "Nandini layout Bangalore" "9886615963" "prakash@gmail.com" "male" "cricket" "baseball"]]))
(defn addpersons [persons]
  (let [conn (d/connect uri)]
    (loop [lx persons]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-person conn (first lx))
      (recur (rest lx))))))

(def sports (map #(zipmap [:sport-name :details] %) [["cricket" "Game of glorious uncertainties"]
                                                     ["football" "Needs great strenght and skill"]
                                                     ["tennis" "Game of grace and speed"]
                                                     ["tabletennis" "Game of grace"]
                                                     ["badminton" "Game of grace speed and skill"]]))

(defn addsports [sports]
  (let [conn (d/connect uri)]
    (loop [lx sports]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-sport conn (first lx))
      (recur (rest lx))))))

(def roles (map #(zipmap [:role-name :details] %) [["administrator" "The administrator"]
                                                     ["player" "The player"]
                                                     ["organiser" "The organiser"]
                                                     ["coach" "The coach"]
                                                     ["scorer" "The scorer"]
                                                     ["statistician" "The statistician"]
                                                     ["umpire" "The umpire"]]))



(defn addroles [roles]
  (let [conn (d/connect uri)]
    (loop [lx roles]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-role conn (first lx))
      (recur (rest lx))))))

(def teams (map #(zipmap [:team-name :sport_ref :person_ref] %) [["rcb" "cricket" "raghu"]
                                                                 ["rcb" "cricket" "prakash"]
                                                                 ["mi" "cricket" "ramesh"]
                                                                 ["csk" "cricket" "anand"]]))

(defn addteams [teams]
  (let [conn (d/connect uri)]
    (loop [lx teams]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-team conn (first lx))
      (recur (rest lx))))))

(def tourneys (map #(zipmap [:tourney-name :sport_ref :person_ref :startdate :enddate :city :address :pin] %)
                       [["ipl2013" "cricket" "raghu" "" "" "bangalore" "KSCA" "560065"]
                        ["ipl2015" "cricket" "prakash" "" "" "chennai" "TNCA" "400088"]
                        ["Under-19" "cricket" "prakash" "" "" "chennai" "TNCA" "400088"]
                        ["Under-21" "cricket" "prakash" "" "" "chennai" "TNCA" "400088"]
                        ["Inter School cricket tournament" "cricket" "prakash" "" "" "chennai" "TNCA" "400088"]]))

(defn addtourneys [tourneys]
  (let [conn (d/connect uri)]
    (loop [lx tourneys]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-event conn (first lx))
      (recur (rest lx))))))

(def participants (map #(zipmap [:team_ref :person_ref :role_ref :tourney_ref] %)
                       [["rcb" "raghu" "player" "ipl2015"]
                        ["rcb" "prakash" "player" "ipl2015"]
                        ["mi" "ramesh" "player" "ipl2015"]
                        ["csk" "anand" "player" "ipl2015"]]))

(defn addparticipants [participants]
  (let [conn (d/connect uri)]
    (loop [lx participants]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-participant conn (first lx))
      (recur (rest lx))))))

(def matches (map #(zipmap [:match-name :team_ref :tourney_ref] %)
                       [["quarter-final" "rcb" "ipl2015"]
                        ["quarter-final""csk" "ipl2015"]
                        ["semi-final" "csk" "ipl2015"]
                        ["semi-final" "rcb" "ipl2015"]]))

(defn addmatches [matches]
  (let [conn (d/connect uri)]
    (loop [lx matches]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-match conn (first lx))
      (recur (rest lx))))))

(def scores (map #(zipmap [:match_ref :log :comments] %)
                       [["quarter-final" "12-1" "Great batting "]
                        ["quarter-final" "32-1" "Great batting "]
                        ["semi-final" "92-9" "Superb bowling "]
                        ["semi-final" "221-1" "Fantastic total "]]))

(defn addscores [scores]
  (let [conn (d/connect uri)]
    (loop [lx scores]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-score conn (first lx))
      (recur (rest lx))))))

(defn dbseed []
  (addpersons persons)
  (addsports sports)
  (addroles roles)
  (addteams teams)
  (addtourneys tourneys)
  (addparticipants participants)
  (addmatches matches)
  (addscores scores)
)


(defn get_tourneys []
  (let [conn (d/connect uri)]
  (comment
    (timbre/info (loop [results (seq (reng/find-tourneys conn))]
                   (when (> (count results) 0)
                     (timbre/info (first results))
                   (recur (rest results)))))
      (timbre/info (loop [lx results]
                   (when (> (count lx) 0)
                     (timbre/info (first lx))
                   (recur (rest lx)))))
    )
    (def results (map #(zipmap [:eid :tourney-name :organiser_name :address :pin :sport :city] %) (reng/find-tourneys conn)))
    (into [] results))
  )

(defn get_tourneys_by_sport [sport_name]
  (let [conn (d/connect uri)]
    (def results (map #(zipmap [:eid :tourney-name :organiser_name :address :pin :sport :city] %) (reng/find-tourneys-by-sport conn sport_name)))
    (into [] results))
  )

(defn get_tourneys_by_pin [pin]
  (let [conn (d/connect uri)]
    (def results (map #(zipmap [:eid :tourney-name :organiser_name :address :pin :sport :city] %) (reng/find-tourneys-by-pin conn pin)))
    (into [] results))
  )

(defn get_tourneys_by_city [city]
  (let [conn (d/connect uri)]
    (def results (map #(zipmap [:eid :tourney-name :organiser_name :address :pin :sport :city] %) (reng/find-tourneys-by-city conn city)))
    (into [] results))
  )


(defn xform_image_loc [loc]
  (timbre/info (:uri (get (:display_sizes loc) 0)))
  {:uri (:uri (get (:display_sizes loc) 0))}
)


(defn get_image_urls [imgvec]
  (map #(xform_image_loc  %) imgvec)
)

(defn download_images [src_token]

  (timbre/info "FETCHING IMAGES from GETTY")
  ;;(def url "https://api.gettyimages.com:443/v3/search/images?embed_content_only=true&orientations=PanoramicHorizontal&prestige_content_only=true&phrase=")
  (def url "https://api.gettyimages.com:443/v3/search/images?embed_content_only=true&orientations=Horizontal&phrase=")
  (def url "https://api.gettyimages.com:443/v3/search/images/editorial?editorial_segments=sport&orientations=PanoramicHorizontal")
  ;(def getty_url (str "https://api.gettyimages.com:443/v3/search/images/editorial?orientations=PanoramicHorizontal&phrase=" src_token))
  ;(def getty_url (str "https://api.gettyimages.com:443/v3/search/images/editorial?editorial_segments=sport&embed_content_only=true&graphical_styles=photography&orientations=PanoramicHorizontal&phrase=" src_token))
  (def getty_url (str url src_token))
  (let [options {:headers {"Api-Key" "kvvexjcn8hzmffbpq73xmpeu"}}
      {:keys [status headers body error]} @(http/get url options)]
    (if error
      (println "Failed, exception is " error)
      (get_image_urls (:images (json/read-str body :key-fn keyword))))
  )
)
(comment)
(def todays_images (concat (download_images "football") (download_images "cricket") (download_images "tennis") (download_images "athletics") (download_images "badminton")))

(timbre/info todays_images)

(defn get_images []
(json/write-str todays_images)
)

(defn fetch_images [query_chan]
  (go (timbre/info "sleeping...")
        ;(Thread/sleep (rand-int 5000))
        (>! query_chan (get_images))
    )
)

(defn search_events_by_sport [query_chan sport]
  (go (timbre/info "querying by sport...")
        ;(Thread/sleep (rand-int 5000))
        (>! query_chan (get_tourneys_by_sport sport))
    )
)

(defn search_events_by_city [query_chan city]
  (go (timbre/info "querying by city...")
        ;(Thread/sleep (rand-int 5000))
        (>! query_chan (get_tourneys_by_city city))
    )
)

(defn search_events_by_pin [query_chan pin]
  (go (timbre/info "querying by pin...")
        ;(Thread/sleep (rand-int 5000))
        (>! query_chan (get_tourneys_by_pin pin))
    )
)


(defn async_query []
  (let [query_chan (chan)]
    (go (timbre/info "sleeping...")
        ;(Thread/sleep (rand-int 5000))
        (>! query_chan (get_images))
    )
    query_chan)
)


(defn dbquery []
  (comment
  (let [conn (d/connect uri)]

    (timbre/info (reng/find-persons conn))
    (timbre/info (reng/find-person-eid conn "raghu"))

    (timbre/info (reng/find-sports conn))
    (timbre/info (reng/find-sport-eid conn "tennis"))

    (timbre/info (reng/find-roles conn))
    (timbre/info (reng/find-role-eid conn "coach")))
  )
  (timbre/info "All" (get_tourneys))
  (timbre/info "By sport" (get_tourneys_by_sport "cricket"))
  (timbre/info "By Pin" (get_tourneys_by_pin "560065"))
  (timbre/info "By city" (get_tourneys_by_city "chennai"))
)
