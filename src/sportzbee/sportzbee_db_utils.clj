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
;(def uri "datomic:mem://sportzbee-db")
;(def sportzbeedb (d/create-database "datomic:mem://sportzbee-db"))
;;(def uri "datomic:ddb://us-east-1/datomic-test/datomic-test-db?aws_secret_key=QGf0q2Gk0NbnLtdJokq7uhgfBpbR1xSM08YTIbQX&aws_access_key_id=AKIAJAHZUGNTLVQBYYPA")
;;
;;(def uri "datomic:ddb://us-east-1/datomic-test/datomic-test-db")
;;(def uri "datomic:free://52.24.45.206:4334/sportzbee")
(def uri "datomic:ddb://ap-southeast-1/datomic-test/datomic-test-db?aws_secret_key=QGf0q2Gk0NbnLtdJokq7uhgfBpbR1xSM08YTIbQX&aws_access_key_id=AKIAJAHZUGNTLVQBYYPA")

(defn listdbs []
 ;; (timbre/info "EXISTING dbs" (d/get-database-names "datomic:free://localhost:4334/*")))
  ;(timbre/info "EXISTING dbs" (d/get-database-names "datomic:mem://*")))
 ;; (timbre/info "EXISTING dbs" (d/get-database-names "datomic:free://52.24.45.206:4334/*")))
 ;; (timbre/info "EXISTING dbs" (d/get-database-names "datomic:ddb://us-east-1/datomic-test/*")))
  (timbre/info "EXISTING dbs" (d/get-database-names "datomic:ddb://ap-southeast-1/datomic-test/*")))

(defn load-schema [dburi]
  (let [conn (d/connect dburi)
        schema (load-file "resources/datomic/sb_schema.edn")]
      (d/transact conn schema)
      conn))

(defn setup-db [dburi]
  (timbre/info "CREATING db " dburi (d/create-database dburi)))

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

(defrecord Person [person-name age address phone email morf sports likes])
(def persons (list (->Person "raghu" 42 "Iskcon Gokulam Bangalore" "9886615961" "raghu@gmail.com" "male" "cricket" "football")
                   (->Person "anand" 42 "Aundh Pune" "9845272298" "anand@gmail.com" "male" "cricket" "tennis")
                   (->Person "ramesh" 43 "Basaveshwarnagar Bangalore" "9886615962" "ramesh@gmail.com" "male" "cricket" "tt")
                   (->Person "prakash" 42 "Nandini layout Bangalore" "9886615963" "prakash@gmail.com" "male" "cricket" "baseball")))

(defn addpersons [persons]
  (let [conn (d/connect uri)]
    (loop [lx persons]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-person conn (first lx))
      (recur (rest lx))))))

(defrecord Sport [sport-name details])
(def sports (list (->Sport "cricket" "Game of glorious uncertainties")
                  (->Sport "football" "Needs great strenght and skill")
                  (->Sport "tennis" "Game of grace and speed")
                  (->Sport "tabletennis" "Game of grace")
                  (->Sport "badminton" "Game of grace speed and skill")))

(defn addsports [sports]
  (let [conn (d/connect uri)]
    (loop [lx sports]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-sport conn (first lx))
      (recur (rest lx))))))

(defrecord Role [role-name details])
(def roles (list (->Role "administrator" "The administrator")
                 (->Role "player" "The player")
                 (->Role "organiser" "The organiser")
                 (->Role "coach" "The coach")
                 (->Role "scorer" "The scorer")
                 (->Role "statistician" "The statistician")
                 (->Role "umpire" "The umpire")))

(defn addroles [roles]
  (let [conn (d/connect uri)]
    (loop [lx roles]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-role conn (first lx))
      (recur (rest lx))))))

(defrecord Team [team-name sport_ref person_ref] )
(def teams (list (->Team "rcb" "cricket" "raghu")
                 (->Team "rcb" "cricket" "prakash")
                 (->Team "mi" "cricket" "ramesh")
                 (->Team "csk" "cricket" "anand")))

(defn addteams [teams]
  (let [conn (d/connect uri)]
    (loop [lx teams]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-team conn (first lx))
      (recur (rest lx))))))

(defrecord Tourney [tourney-name sport_ref person_ref startdate enddate city address pin])
(def tourneys (list (->Tourney "ipl2015" "cricket" "raghu" "" "" "bangalore" "KSCA" "560065")
                    (->Tourney "Under-19" "cricket" "prakash" "" "" "chennai" "TNCA" "400088")
                    (->Tourney "Under-21" "cricket" "prakash" "" "" "chennai" "TNCA" "400088")
                    (->Tourney "Inter School cricket tournament" "cricket" "prakash" "" "" "chennai" "TNCA" "400088")
                    ))

(defn addtourneys [tourneys]
  (let [conn (d/connect uri)]
    (loop [lx tourneys]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-event conn (first lx))
      (recur (rest lx))))))

(defrecord Participant [team_ref person_ref role_ref tourney_ref])
(def participants (list (->Participant "rcb" "raghu" "player" "ipl2015")
                        (->Participant "rcb" "prakash" "player" "ipl2015")
                        (->Participant "mi" "ramesh" "player" "ipl2015")
                        (->Participant "csk" "anand" "player" "ipl2015")))

(defn addparticipants [participants]
  (let [conn (d/connect uri)]
    (loop [lx participants]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-participant conn (first lx))
      (recur (rest lx))))))

(defrecord Match [match-name team_ref tourney_ref])
(def matches (list (->Match "quarter-final" "rcb" "ipl2015")
                   (->Match "quarter-final""csk" "ipl2015")
                   (->Match "semi-final" "csk" "ipl2015")
                   (->Match "semi-final" "rcb" "ipl2015")))

(defn addmatches [matches]
  (let [conn (d/connect uri)]
    (loop [lx matches]
      (when (> (count lx) 0)
      (timbre/info "Member -> " (first lx))
      (reng/add-match conn (first lx))
      (recur (rest lx))))))

(defrecord Score [match_ref log comments])
(def scores (list (->Score "quarter-final" "12-1" "Great batting ")
                  (->Score "quarter-final" "32-1" "Great batting ")
                  (->Score "semi-final" "92-9" "Superb bowling ")
                  (->Score "semi-final" "221-1" "Fantastic total ")))

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
    (def results (map #(zipmap [:eid :tourney-name :organiser_name :address :pin :sport :city] %) (reng/find-tourneys conn))))
    (into [] results)
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
;(defn get_images [])
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
