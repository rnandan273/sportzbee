(ns sportzbee.datomic_utils
  (:require [clojure.data.json :as json])
  (:require [taoensso.timbre :as timbre])
  (:require [sportzbee.reco_engine :as reng])
  (:require [datomic.api :as d])
  (:require [datomic.api :only [q db] :as d])
  (:require [clojure.data.csv :as csv])
  (:require [clojure.java.io :as io])
  (:use [clojure.pprint :as pprint])
)

;;(def uri "datomic:free://localhost:4334/rengine")
(def uri "datomic:mem://reco-db")
(defn listdbs []
  ;;(timbre/info "EXISTING dbs" (d/get-database-names "datomic:free://localhost:4334/*")))
(timbre/info "EXISTING dbs" (d/get-database-names "datomic:mem://*")))

(defn load-schema [dburi]
  (let [conn (d/connect dburi)
        schema (load-file "resources/datomic/schema.edn")]
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

(defn show_style_item [item]
  (timbre/info (:bottom item) (:top item)))

(defn show_styles [style_trend styles_list]
  ;;loop through the sequence and add the styles to the datomic database
  (let [conn (d/connect uri)]
  (loop [list_x styles_list]
    (when (> (count list_x) 0)
      (cond
       (= style_trend "summer") (reng/add-summer-style conn (first list_x))
       (= style_trend "workwear") (reng/add-workwear-style conn (first list_x))
       (= style_trend "festival") (reng/add-festival-style conn (first list_x)))

      (recur (rest list_x))))))

(defn list_styles []
  (timbre/info "Listing styles generated")

  (show_styles "summer" (reng/get-summer-trends))
  (show_styles "festival" (reng/get-festival-trends))
  (show_styles "workwear" (reng/get-workwear-trends))

  (println (count (reng/get-workwear-trends))
           (count (reng/get-festival-trends))
           (count (reng/get-summer-trends)))
 )

(defn show_product_detail [item]
  (timbre/info item))

(defn show_products [product_type product_list]
  (let [conn (d/connect uri)]
    (loop [list_x product_list]
      (when (> (count list_x) 0)
        (cond
         (= product_type "tops")(reng/add-product-top conn (first list_x))
         (= product_type "bottoms")(reng/add-product-bottom conn (first list_x))
         (= product_type "dress")(reng/add-product-dress conn (first list_x))
         (= product_type "accessory")(reng/add-product-accessory conn (first list_x))
         (= product_type "footwear")(reng/add-product-footwear conn (first list_x)))
        (recur (rest list_x)))))
  )

(defn list_products []
  (timbre/info "Listing products generated")
  (show_products "tops" (reng/get-products-tops))
  (show_products "bottoms" (reng/get-products-bottoms))
  (show_products "dress" (reng/get-products-dress))
  (show_products "accessory" (reng/get-products-accessory))
  (show_products "footwear" (reng/get-products-footwear))

  (println (count (reng/get-products-tops))
           (count (reng/get-products-bottoms))
           (count (reng/get-products-dress))
           (count (reng/get-products-accessory))
           (count (reng/get-products-footwear))))

(defn dbseed []
  (list_styles)
  (list_products))

(defn dbquery []
  (let [conn (d/connect uri)]
  (timbre/info (reng/find-all-styles conn))
  (timbre/info (reng/find-all-products conn))))

(defn importcsv []
  (timbre/info "Importing bloomingdale inventory")
  (with-open [in-file (io/reader "/Users/raghu/work/projects/clojure/luminus_sportz/sportzbee/resources/reco/inventory.csv")]
  (doall
    (csv/read-csv in-file))
  ))

(defn mapproduct [x]
  {:id (get x 0) :title (get x 1) :type (get x 2)
   :style_type (get x 3) :product_url (get x 4) :descr (get x 5)
   :color (get x 6) :size (get x 7) :keywords (get x 8) :image_name (get x 9)})

(defn importproducts []
  (->> (importcsv)
       (map mapproduct))
  )

(defn insertproducts []
  (let [conn (d/connect uri)]
    (loop [list_x (importproducts)]
      (when (> (count list_x) 0)
        ;;(cond
         ;;(= product_type "tops")(reng/add-product-top conn (first list_x))
        (println "Item ->" (:type (first list_x)))
        (recur (rest list_x))))))
