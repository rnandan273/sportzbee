(ns sportzbee.ml_utils
  (:require [clojure.data.json :as json])
  (:require [taoensso.timbre :as timbre])
  (:require [datomic.api :as d])
  (:require [datomic.api :only [q db] :as d])
  (:require [clojure.data.csv :as csv])
  (:require [clojure.java.io :as io])
  (:use [clj-ml classifiers data])
  (:use [clojure.pprint :as pprint])
)


(def tops (vector "peasant tops" "off shoulders"  "tank"
                  "Basic" "Long sleeves" "Three quartet"
                  "Croppes shell tank" "Neckline U neck"  "crew neck"  "sleeveless"  "plain solid colored tank tops" "fashion vests"
                ))

(def bottoms (vector "boyfrienddenim"
                     "chinos" "pencil skirts" "ankle pants"
                     "Printed Pallazo soft pants" "Gauchos" "pencil skirts" "ankle pants"
                     ))

(def patterns (vector "crochet" "lace" "embroidery"
                      ""
                      ""))

(def dresses (vector "line" "rompers" "skaters"
                     "shift" "sheath" "shirt" "line"
                     ""
   ))

(def fits (vector ""
                  "flare"  "straight" "skinny" "wide leg"
                  "flare" "straight" "wide leg"))

(def prints (vector ""
                    ""
                    "Zig-zag" "Florals" "Paisley"))

(def colors (vector ""
                    "Solid Plain Colors" "Dark" "Blues" "Browns" "Black" "Neutral" "Greys"
                    "Multi Colors" "Whites" "Blues"))

(def accessories (vector "Tote Bags"
                         "analog time watch"
                         "Fringe Macrame Tote handbag"))

(def footwear (vector "Gladiator Sandals" "Flat Bed Sandals" "Thing Sandals"
                       "platform" "high" "wedges" "pumps"
                       "Thing Sandals" "Footbed sandals"))

(def star_rating [:5_star :4_star :3_star :2_star :1_star])


(defn rand-in-range [min max]
  (let [len (- max min)
        rand-len (rand-int len)]
    (+ min rand-len)))

(defn create-summer-spec []
  (vector :summer
          (rand-in-range 0 3) ;;tops
          (rand-in-range 0 0) ;; bottoms
          (rand-in-range 0 3) ;;patterns
          (rand-in-range 0 3) ;;dresses
          (rand-in-range 0 0) ;;fit
          (rand-in-range 0 0) ;;prints
          (rand-in-range 0 0) ;;colors
          (rand-in-range 0 0) ;;accessory
          (rand-in-range 0 3) ;;footwear
          (get star_rating (rand-in-range 3 5)) ;; rating
          ))

(defn create-non-summer-spec []
  (vector :nocategory
          (rand-in-range 3 12) ;;tops
          (rand-in-range 1 8) ;; bottoms
          (rand-in-range 3 5) ;;patterns
          (rand-in-range 3 8) ;;dresses
          (rand-in-range 1 8) ;;fit
          (rand-in-range 1 5) ;;prints
          (rand-in-range 1 11) ;;colors
          (rand-in-range 1 3) ;;accessory
          (rand-in-range 3 9) ;;footwear
          (get star_rating (rand-in-range 0 2)) ;; rating
          ))

(defn create-workwear-spec []
  (vector :workwear
          (rand-in-range 3 6) ;;tops
          (rand-in-range 1 3) ;; bottoms
          (rand-in-range 0 0) ;;patterns
          (rand-in-range 3 7) ;;dresses
          (rand-in-range 1 5) ;;fit
          (rand-in-range 0 0) ;;prints
          (rand-in-range 1 8) ;;colors
          (rand-in-range 1 1) ;;accessory
          (rand-in-range 3 7) ;;footwear
          (get star_rating (rand-in-range 3 5)) ;; rating
          ))

(defn create-non-workwear-spec []
  (vector :nocategory
          (rand-in-range 6 12) ;;tops
          (rand-in-range 3 8) ;; bottoms
          (rand-in-range 1 5) ;;patterns
          (rand-in-range 7 8) ;;dresses
          (rand-in-range 5 9) ;;fit
          (rand-in-range 2 5) ;;prints
          (rand-in-range 8 11) ;;colors
          (rand-in-range 1 3) ;;accessory
          (rand-in-range 0 3) ;;footwear
          (get star_rating (rand-in-range 0 2)) ;; rating
          ))

(defn create-festival-spec []
  (vector :festival
          (rand-in-range 6 12) ;;tops
          (rand-in-range 5 9) ;; bottoms
          (rand-in-range 0 0) ;;patterns
          (rand-in-range 7 7) ;;dresses
          (rand-in-range 5 8) ;;fit
          (rand-in-range 2 5) ;;prints
          (rand-in-range 8 11) ;;colors
          (rand-in-range 2 2) ;;accessory
          (rand-in-range 7 9) ;;footwear
          (get star_rating (rand-in-range 3 5)) ;; rating
          ))

(defn create-non-festival-spec []
  (vector :nocategory
          (rand-in-range 0 6) ;;tops
          (rand-in-range 0 5) ;; bottoms
          (rand-in-range 1 5) ;;patterns
          (rand-in-range 0 7) ;;dresses
          (rand-in-range 0 5) ;;fit
          (rand-in-range 0 2) ;;prints
          (rand-in-range 0 8) ;;colors
          (rand-in-range 0 2) ;;accessory
          (rand-in-range 0 7) ;;footwear
          (get star_rating (rand-in-range 0 2)) ;; rating
          ))

(defn create-sample-spec []
  (let [x (rand-in-range 0 6)]
    (cond (= x 0) (create-summer-spec)
          (= x 1) (create-workwear-spec)
          (= x 2) (create-festival-spec)
          (= x 3) (create-non-workwear-spec)
          (= x 4) (create-non-summer-spec)
          (= x 5) (create-non-festival-spec))))

(defn create-random-spec []
  (vector :nocategory (rand-int (count tops))
                                            (rand-int (count bottoms))
                                            (rand-int (count patterns))
                                            (rand-int (count dresses))
                                            (rand-int (count fits))
                                            (rand-int (count prints))
                                            (rand-int (count colors))
                                            (rand-int (count accessories))
                                            (rand-int (count footwear))
                                            (get star_rating (rand-int 5)))
  )

(defn get-category-index [coll kwv]
  (if (= (.indexOf coll kwv) -1)
         (rand-int (count coll))
         (.indexOf coll kwv))
  )

(defn create-random-keyword-spec [kvmap]
  (vector :nocategory (if (contains? kvmap :tops)
                        (get-category-index tops (:tops kvmap))
                        (rand-int (count tops)))
                      (if (contains? kvmap :bottoms)
                        (get-category-index bottoms (:bottoms kvmap))
                        (rand-int (count bottoms)))
                      (if (contains? kvmap :patterns)
                        (get-category-index patterns (:patterns kvmap))
                        (rand-int (count patterns)))
                      (if (contains? kvmap :dresses)
                        (get-category-index dresses (:dresses kvmap))
                        (rand-int (count dresses)))
                      (if (contains? kvmap :fits)
                        (get-category-index fits (:fits kvmap))
                        (rand-int (count fits)))
                      (if (contains? kvmap :prints)
                        (get-category-index prints (:prints kvmap))
                        (rand-int (count prints)))
                      (if (contains? kvmap :colors)
                        (get-category-index colors (:colors kvmap))
                        (rand-int (count colors)))
                      (if (contains? kvmap :accessories)
                        (get-category-index accessories (:accessories kvmap))
                        (rand-int (count accessories)))
                      (if (contains? kvmap :footwear)
                        (get-category-index footwear (:footwear kvmap))
                        (rand-int (count footwear)))
                      (get star_rating (rand-int 5))) ;; rating
  )


(def fashion-template
  [{:category [:summer :workwear :festival :nocategory]}
    :tops :bottom :patterns :dress :fit :prints :colors :accessory :footwear {:rating [:5_star :4_star :3_star :2_star :1_star]}])
;;BAYES
(def bayes-classifier (make-classifier :bayes :naive {:supervised-discretization true}))


(def fashion-training-data
  (for [i (range 10000)]
    (create-sample-spec)))

(get star_rating (rand-int 5))
(get star_rating (rand-int 1))
(comment)

(def fashion-dataset  (make-dataset "fashion" fashion-template fashion-training-data))

(defn train_bayes_classifier []
  (dataset-set-class fashion-dataset 10)
  (classifier-train bayes-classifier fashion-dataset))

;;DECISION TREE
(def DT-classifier (make-classifier :decision-tree :c45 {:unpruned true}))

(defn train_DT_classifier []
  (dataset-set-class fashion-dataset 10)
  (classifier-train DT-classifier fashion-dataset))

(def sample-user
  (make-instance fashion-dataset [:festival (rand-int (count tops))
                                            (rand-int (count bottoms))
                                            (rand-int (count patterns))
                                            (rand-int (count dresses))
                                            (rand-int (count fits))
                                            (rand-int (count prints))
                                            (rand-int (count colors))
                                            (rand-int (count accessories))
                                            (rand-int (count footwear))
                                            (get star_rating (rand-int 5))]))

(train_bayes_classifier)
(train_DT_classifier)

(defn recommend_products []
  (timbre/info "Rating is -> " (classifier-classify bayes-classifier sample-user))
)


(defn test_config []
   ;;(timbre/info tops)
  (timbre/info  (create-summer-spec))
  (timbre/info  (create-workwear-spec))
  (timbre/info  (create-festival-spec))

   (recommend_products)
  (timbre/info "FASHION DATASET")

 ;; (classifier-classify bayes-classifier sample-user)
 ;; (classifier-classify bayes-classifier sample-user)
 ;; (timbre/info (fashion-dataset))
  (timbre/info "Ratings USING BAYES CLASSIFIER")
  (timbre/info (->> (create-random-spec)
       (make-instance fashion-dataset)
       (classifier-classify bayes-classifier)))

  (timbre/info (->> (create-random-spec)
       (make-instance fashion-dataset)
       (classifier-classify bayes-classifier)))

  (timbre/info (->> (create-random-keyword-spec {:bottoms "chinos" :fits "straight"})
       (make-instance fashion-dataset)
       (classifier-classify bayes-classifier)))


  (timbre/info ":Ratings USING DECISION TREE")

  (timbre/info (->> (create-random-spec)
       (make-instance fashion-dataset)
       (classifier-classify DT-classifier)))

  (timbre/info (->> (create-random-spec)
       (make-instance fashion-dataset)
       (classifier-classify DT-classifier)))

  (timbre/info (->> (create-random-keyword-spec {:bottoms "chinos" :fits "straight"})
       (make-instance fashion-dataset)
       (classifier-classify DT-classifier)))
)




