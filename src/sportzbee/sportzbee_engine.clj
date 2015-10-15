(ns sportzbee.sportzbee_engine
  (:require [taoensso.timbre :as timbre])
  (:require [datomic.api :as d])
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:use [clojure.core.logic.pldb]))

(def conn nil)

;;add person

(defn add-person [conn person_obj]
  (timbre/info "Adding Person -> " {
                      :person/name (:person-name person_obj)
                      :person/age (:age person_obj)
                      :person/address (:address person_obj)
                      :person/phone (:phone person_obj)
                      :person/email (:email person_obj)
                      :person/sex (:morf person_obj)
                      :person/sports (:sports person_obj)
                      :person/likes (:likes person_obj)
                             })

  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :person/name (:person-name person_obj)
                      :person/age (:age person_obj)
                      :person/address (:address person_obj)
                      :person/phone (:phone person_obj)
                      :person/email (:email person_obj)
                      :person/sex (:morf person_obj)
                      :person/sports (:sports person_obj)
                      :person/likes (:likes person_obj)
                      }]))

;;queries
(defn find-person-eid [conn person-name]
  (timbre/info "Searching person -> " person-name)
  (d/q '[:find ?eid
         :in $ ?person-name
         :where [?eid :person/name ?person-name]]
       (d/db conn)
       person-name))

(defn find-persons [conn]
  (d/q '[:find ?eid ?person-name
         :in $
         :where [?eid :person/name ?person-name]]
       (d/db conn)))

;;add sport
(defn add-sport [conn sport_obj]
  (timbre/info "Adding Sport ->" {:sport/name (:sport-name sport_obj)
                      :sport/details (:details sport_obj)})

  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :sport/name (:sport-name sport_obj)
                      :sport/details (:details sport_obj)}]))

;;queries
(defn find-sport-eid [conn a_name]
  (timbre/info "Searching sport -> " a_name)
  (d/q '[:find ?eid
         :in $ ?a_name
         :where [?eid :sport/name ?a_name]]
       (d/db conn)
       a_name))

(defn find-sports [conn]
  (d/q '[:find ?eid ?a_name
         :in $
         :where [?eid :sport/name ?a_name]]
       (d/db conn)))

;;add role
(defn add-role [conn role_obj]
  (timbre/info "Adding Role ->" {:role/name (:role-name role_obj)
                      :role/details (:details role_obj)})

  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :role/name (:role-name role_obj)
                      :role/details (:details role_obj)}]))

;;queries
(defn find-role-eid [conn a_name]
  (d/q '[:find ?eid
         :in $ ?a_name
         :where [?eid :role/name ?a_name]]
       (d/db conn)
       a_name))

(defn find-roles [conn]
  (d/q '[:find ?eid ?a_name
         :in $
         :where [?eid :role/name ?a_name]]
       (d/db conn)))

;;add tourney
(defn add-event [conn event_obj]
  (timbre/info (ffirst (find-sport-eid conn (:sport_ref event_obj))))
  (timbre/info (ffirst (find-person-eid conn (:person_ref event_obj))))
  (timbre/info "Time string" (read-string "#inst \"2012-06-12T11:51:26.00Z\""))

  (let [sport_ref (ffirst (find-sport-eid conn (:sport_ref event_obj)))
        person_ref (ffirst (find-person-eid conn (:person_ref event_obj)))]

  (timbre/info "Adding Tournament -> " {:tourney/name (:tourney-name event_obj)
                       :tourney/startdate (read-string "#inst \"2012-06-12T11:51:26.00Z\"")
                       :tourney/enddate (read-string "#inst \"2012-06-12T11:51:26.00Z\"")
                       :tourney/city (:city event_obj)
                       :tourney/address (:address event_obj)
                       :tourney/pin (:pin event_obj)
                       :tourney/sport sport_ref
                       :tourney/organiser person_ref})


  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                       :tourney/name (:tourney-name event_obj)
                       :tourney/organiser person_ref
                       :tourney/startdate (read-string "#inst \"2012-06-12T11:51:26.00Z\"")
                       :tourney/enddate (read-string "#inst \"2012-06-12T11:51:26.00Z\"")
                       :tourney/city (:city event_obj)
                       :tourney/address (:address event_obj)
                       :tourney/pin (:pin event_obj)
                       :tourney/sport sport_ref}]))
  )

;;queries
(defn find-tourney-eid [conn a_name]
  (d/q '[:find ?eid
         :in $ ?a_name
         :where [?eid :tourney/name ?a_name]]
       (d/db conn)
       a_name))

(defn find-tourneys [conn]
  (d/q '[:find ?eid ?a_name ?organiser ?address ?pin ?sport ?city
         :in $
         :where [?eid :tourney/name ?a_name]
                [?eid :tourney/organiser ?org_ref]
                [?org_ref :person/name ?organiser]
                [?eid :tourney/address ?address]
                [?eid :tourney/pin ?pin]
                [?eid :tourney/sport ?sport_ref]
                [?sport_ref :sport/name ?sport]
                [?eid :tourney/city ?city]]
       (d/db conn)))

;;add team
(defn add-team [conn team_obj]

  (timbre/info (ffirst (find-sport-eid conn (:sport_ref team_obj))))
  (timbre/info (ffirst (find-person-eid conn (:person_ref team_obj))))

  (let [sport_ref (ffirst (find-sport-eid conn (:sport_ref team_obj)))
        person_ref (ffirst (find-person-eid conn (:person_ref team_obj)))]

  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                       :team/name (:team-name team_obj)
                       :team/sport sport_ref
                       :team/player person_ref}])
  )
)

;;queries
(defn find-team-eid [conn a_name]
  (d/q '[:find ?eid
         :in $ ?a_name
         :where [?eid :team/name ?a_name]]
       (d/db conn)
       a_name))

(defn find-teams [conn]
  (d/q '[:find ?eid ?a_name
         :in $
         :where [?eid :team/name ?a_name]]
       (d/db conn)))

;;add participant

(defn add-participant [conn p_obj]
  (timbre/info (ffirst (find-role-eid conn (:role_ref p_obj))))
  (timbre/info (ffirst (find-person-eid conn (:person_ref p_obj))))
  (timbre/info (ffirst (find-team-eid conn (:team_ref p_obj))))
  (timbre/info (ffirst (find-tourney-eid conn (:tourney_ref p_obj))))

  (let [person_ref (ffirst (find-person-eid conn (:person_ref p_obj)))
        role_ref (ffirst (find-role-eid conn (:role_ref p_obj)))
        team_ref (ffirst (find-team-eid conn (:team_ref p_obj)))
        tourney_ref (ffirst (find-tourney-eid conn (:tourney_ref p_obj)))]

  (timbre/info "Adding Participant ->"
                      {:participant/person person_ref
                       :participant/role role_ref
                       :participant/team team_ref
                       :participant/tourney tourney_ref})

  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                       :participant/person person_ref
                       :participant/role role_ref
                       :participant/team team_ref
                       :participant/tourney tourney_ref}])
    )

  )
;;add match

(defn add-match [conn match_obj]
   (timbre/info "Adding Match -> " match_obj)

  (let [team_ref (ffirst (find-team-eid conn (:team_ref match_obj)))
        tourney_ref (ffirst (find-tourney-eid conn (:tourney_ref match_obj)))]

  (timbre/info "Adding Match -> " {:match/name (:match-name match_obj)
                       :match/tourney tourney_ref
                       :match/team team_ref})

  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                       :match/name (:match-name match_obj)
                       :match/tourney tourney_ref
                       :match/team team_ref}])))
;;queries
(defn find-match-eid [conn a_name]
  (d/q '[:find ?eid
         :in $ ?a_name
         :where [?eid :match/name ?a_name]]
       (d/db conn)
       a_name))

(defn find-matches [conn]
  (d/q '[:find ?eid ?a_name
         :in $
         :where [?eid :match/name ?a_name]]
       (d/db conn)))

;;add score

(defn add-score [conn score_obj]
  (let [match_ref (ffirst (find-match-eid conn (:match_ref score_obj)))]

    (timbre/info "Adding Score -> " {:score/match match_ref
                       :score/log (:log score_obj)
                       :score/comments (:comments score_obj)})

   @(d/transact conn [{:db/id (d/tempid :db.part/user)
                       :score/match match_ref
                       :score/log (:log score_obj)
                       :score/comments (:comments score_obj)}]))
  )

;; query scores
(defn find-score-log [conn match_name]
  (d/q '[:find ?eid ?score_log
         :in $
         :where [?eid :score/match ?match_ref]
                [?match_ref :match/name ?match_name]
                [?eid :score/log ?score_log]]
       (d/db conn)
       match_name))
