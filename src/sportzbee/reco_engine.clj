(ns sportzbee.reco_engine
  (:require [taoensso.timbre :as timbre])
  (:require [datomic.api :as d])
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:use [clojure.core.logic.pldb]))

(def conn nil)

(defn add-person [person-name]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :person/name person-name}]))

(defn add-summer-style [conn style]
  (println "Adding summer in DB " )

  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/dress  (:dress style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/rating (rand-int 20)}])

 (println "xxxzzz")
)

(defn add-workwear-style [conn style]
  (println "Adding workwear in DB " style)


  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/dress  (:dress style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/colour (:colour style)
                        :style/fit (:fit style)
                        :style/rating (rand-int 20)}])
 (println "xxxzzz")
)


(defn add-festival-style [conn style]
  (println "Adding festival in DB " style)

  @(d/transact conn [{ :db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/prints (:prints style)
                        :style/fit (:fit style)
                        :style/rating (rand-int 20)}])
)


(defn find-product-id [product-name]
  (ffirst (d/q '[:find ?eid
         :in $ ?product-name
         :where [?eid :product/name ?product-name]]
       (d/db conn)
       product-name)))

(defn add-product-top [conn product]

  (let [{p_brand :brand
         p_top :top
         p_colour :colour
         p_pattern :pattern
         p_category :category} product]

  @(d/transact conn [{
                        :db/id (d/tempid :db.part/user)
                        :product/brand p_brand
                        :product/top p_top
                        :product/colour p_colour
                        :product/pattern p_pattern
                        :product/category p_category}]))
  )

(defn add-product-bottom [conn product]

  (let [{p_brand :brand
         p_bottom :bottom
         p_colour :colour
         p_fit :fit
         p_category :category} product]

  @(d/transact conn [{
                        :db/id (d/tempid :db.part/user)
                        :product/brand p_brand
                        :product/bottom p_bottom
                        :product/colour p_colour
                        :product/fit p_fit
                        :product/category p_category}]))
  )

(defn add-product-dress [conn product]

  (let [{p_brand :brand
         p_dress :dress
         p_colour :colour
         p_category :category} product]

  @(d/transact conn [{
                        :db/id (d/tempid :db.part/user)
                        :product/brand p_brand
                        :product/dress p_dress
                        :product/colour p_colour
                        :product/category p_category}]))
  )

(defn add-product-accessory [conn product]

  (let [{p_brand :brand
         p_accessory :accessory
         p_colour :colour
         p_category :category} product]

  @(d/transact conn [{
                        :db/id (d/tempid :db.part/user)
                        :product/brand p_brand
                        :product/accessory p_accessory
                        :product/colour p_colour
                        :product/category p_category}]))
  )

(defn add-product-footwear [conn product]

  (let [{p_brand :brand
         p_footwear :footwear
         p_colour :colour
         p_category :category} product]

  @(d/transact conn [{
                        :db/id (d/tempid :db.part/user)
                        :product/brand p_brand
                        :product/footwear p_footwear
                        :product/colour p_colour
                        :product/category p_category}]))
  )

(defn find-all-styles[conn]
  (d/q '[:find ?rating
         :where [_ :style/rating ?rating]]
       (d/db conn)))

(defn find-all-products [conn]
  (d/q '[:find ?category1
         :where [_ :product/fit ?category1]]
       (d/db conn)))


(defn find-product-trend[product-name]
  (d/q '[:find ?trend-name
         :in $ ?product-name
         :where [?eid :product/name ?product-name]
                [?eid :product/trendcat ?trend-name]]
       (d/db conn)
       product-name))

(defn find-product-trend-productcode[product-code]
  (d/q '[:find ?trend-name
         :in $ ?product-code
         :where [?eid :product/code ?product-code]
                [?eid :product/trendcat ?trend-name]]
       (d/db conn)
       product-code))

(defn find-product-color[product-color product-category]
  (println product-category)
  (d/q '[:find ?product-category
         :in $ ?product-color
         :where [?eid :product/color ?product-color]
                [?eid :product/category ?product-category]
                 (not [?eid :product/category "Jeans"])]
       (d/db conn)
       product-color))


(defn find-all-persons[]
  (d/q '[:find ?person-name
         :where [_ :person/name ?person-name]]
       (d/db conn)))


(defn find-person-id [person-name]
  (ffirst (d/q '[:find ?eid
         :in $ ?person-name
         :where [?eid :person/name ?person-name]]
       (d/db conn)
       person-name)))


(defn add-person-account [person-name account-name]
  (let [acc-id (d/tempid :db.part/user)]
      @(d/transact conn [{:db/id acc-id :account/name account-name}
                         {:db/id (find-person-id person-name) :person/account acc-id}])))


(defn find-accounts-for-person [person-name]
  (d/q '[:find ?account-name
         :in $ ?person-name
         :where [?eid :person/name ?person-name]
                [?eid :person/account ?account]
                [?account :account/name ?account-name]]
       (d/db conn)
       person-name))

(db-rel top x)
(db-rel brand x)
(db-rel pattern p)
(db-rel bottom b)
(db-rel dress d)
(db-rel accessory a)
(db-rel footwear f)
(db-rel fit ft)
(db-rel colour c)
(db-rel prints pnts)

(def product_top
  (db
   [brand "Proline"]
   [brand "Mark Spencer"]
   [brand "Kohls"]
   [brand "Nautica"]
   [brand "GAP"]
   [brand "GUESS"]
   [brand "Michael Kors"]
   [brand "Gucci"]
   [brand "Prada"]
   [top "peasant tops"]
   [top "off shoulders"]
   [top "tank"]
   [colour "white"]
   [colour "bright red"]
   [colour "pink"]
   [colour "green"]
   [colour "yellow"]
   [pattern "stripes"]
   [pattern "lace"]
   [pattern "embroidery"]
   )
)

(def product_bottom
  (db
   [brand "Zara"]
   [brand "Nautica"]
   [brand "Kohls"]
   [brand "Nautica"]
   [brand "GAP"]
   [brand "GUESS"]
   [brand "Michael Kors"]
   [brand "Gucci"]
   [colour "bright"]
   [colour "white"]
   [colour "bright red"]
   [colour "pink"]
   [colour "green"]
   [colour "yellow"]
   [bottom "chinos"]
   [bottom "pencil skirts"]
   [bottom "ankle pants"]
   [fit "flare"]
   [fit "straight"]
   [fit "skinny"]
   [fit "wide leg"]
   )
)

(def product_dress
  (db
   [brand "Zara"]
   [brand "Kohls"]
   [brand "Kohls"]
   [brand "Nautica"]
   [brand "GAP"]
   [brand "GUESS"]
   [brand "Michael Kors"]
   [brand "Gucci"]
   [colour "white"]
   [colour "bright red"]
   [colour "pink"]
   [colour "green"]
   [colour "yellow"]
   [dress "shift"]
   [dress "sheath"]
   [dress "shirt"]
   [dress "line"]
   [dress "line"]
   [dress "rompers"]
   [dress "skaters"]
   )
)

(def product_accessory
  (db
   [brand "Apple iWatch"]
   [brand "Skagen"]
   [brand "Burberry"]
   [brand "BVLGARI"]
   [brand "Lacoste"]
   [brand "Valentino"]
   [brand "Dior"]
   [colour "Dark"]
   [colour "Brown"]
   [colour "Black"]
   [colour "White"]
   [colour "Pink"]
   [colour "Yellow"]
   [colour "Green"]
   [colour "Grey"]
   [accessory "Handbag"]
   [accessory "Purse"]
   [accessory "Sunglasses"]
   [accessory "Jewellery"]
   [accessory "Scarves"]
   [accessory "Purse"]
   [accessory "Watch"])
)

(def product_footwear
  (db
   [brand "Gucci"]
   [brand "Converse"]
   [brand "Valentino"]
   [brand "Melissa"]
   [brand "Fendi"]
   [colour "Black"]
   [colour "White"]
   [colour "Pink"]
   [colour "Yellow"]
   [colour "Green"]
   [colour "Blue"]
   [colour "Grey"]
   [footwear "Thing Sandals"]
   [footwear "Footbed sandals"]
   [footwear "Shoe - Sneakers"]
   [footwear "Shoe - Leather"]))


(def summer_trend
  (db
   [bottom "boyfrienddenim"]
   [pattern "crochet"]
   [pattern "lace"]
   [pattern "embroidery"]
   [dress "line"]
   [dress "rompers"]
   [dress "skaters"]
   [top "peasant tops"]
   [top "off shoulders"]
   [top "tank"]
   [accessory "Tote Bags"]
   [footwear "Gladiator Sandals"]
   [footwear "Flat Bed Sandals"]
   [footwear "Thing Sandals"]
   )
)

(def workshop_trend
  (db
   [bottom "chinos"]
   [bottom "pencil skirts"]
   [bottom "ankle pants"]
   [fit "flare"]
   [fit "straight"]
   [fit "skinny"]
   [fit "wide leg"]
   [dress "shift"]
   [dress "sheath"]
   [dress "shirt"]
   [dress "line"]
   [footwear "platform"]
   [footwear "high"]
   [footwear "wedges"]
   [footwear "pumps"]
   [accessory "analog time watch"]
   [top "Long sleeves"]
   [top "Three quartet"]
   [top "Basic"]
   [colour "Solid Plain Colors"]
   [colour "Dark"]
   [colour "Blues"]
   [colour "Browns"]
   [colour "Black"]
   [colour "Neutral"]
   [colour "Greys"]
   )
)

(def festival_trend
  (db
   [bottom "Printed Pallazo soft pants"]
   [bottom "Gauchos"]
   [bottom "pencil skirts"]
   [bottom "ankle pants"]
   [fit "flare"]
   [fit "straight"]
   [fit "wide leg"]
   [footwear "Thing Sandals"]
   [footwear "Footbed sandals"]
   [accessory "Fringe Macrame Tote handbag"]
   [top "Croppes shell tank"]
   [top "Neckline U neck"]
   [top "crew neck"]
   [top "sleeveless"]
   [top "plain solid colored tank tops"]
   [top "fashion vests"]
   [colour "Multi Colors"]
   [colour "Whites"]
   [colour "Blues"]
   [prints "Zig-zag"]
   [prints "Florals"]
   [prints "Paisley"]
   )
)

(defn get-products-tops []

      (println "Products Tops :->")

      (with-db product_top []
        (run* [q]
              (fresh [t p b c]
                     (top t)
                     (pattern p)
                     (brand b)
                     (colour c)

                     (== q {:category "tops" :top t :pattern p :brand b :colour c}))))

      )

(defn get-products-bottoms []

      (println "Products Bottom :->")

      (with-db product_bottom []
        (run* [q]
              (fresh [b c f btm]
                     (fit f)
                     (bottom btm)
                     (brand b)
                     (colour c)
                     (== q {:category "bottoms" :bottom btm :fit f :brand b :colour c}))))

      )

(defn get-products-dress []

      (println "Products Dress :->")

      (with-db product_dress []
        (run* [q]
              (fresh [b c d]
                     (dress d)
                     (brand b)
                     (colour c)
                     (== q {:category "dress" :dress d :brand b :colour c}))))

      )

(defn get-products-accessory []
      (println "Products Accessory :->")
      (with-db product_accessory []
        (run* [q]
              (fresh [b c a]
                     (accessory a)
                     (brand b)
                     (colour c)

                     (== q {:category "accessory" :accessory a :brand b :colour c}))))

      )


(defn get-products-footwear []
      (println "Products Footwear :->")
      (with-db product_footwear []
        (run* [q]
              (fresh [b c f]
                     (footwear f)
                     (brand b)
                     (colour c)

                     (== q {:category "footwear" :footwear f :brand b :colour c}))))

      )


(defn pattern_select_condition [p]
  (pattern p)
)


(defn top_select_condition [t]
  (== "tank" t)
)


(defn get-summer-trends []
  (println "Summer  Trends :->")
  (with-db summer_trend []
    (run* [q]
          (fresh [t p b d a f]
                 (top_select_condition t)
                 (pattern_select_condition p)
                 (bottom b)
                 (dress d)
                 (accessory a)
                 (footwear f)
                 (== q {:trend "summer" :top t :pattern p :bottom b :dress d :accessory a :footwear f :rating 1}))))

  )


(defn get-workwear-trends []
  (println "Workwear  Trends :->")
  (with-db workshop_trend []
    (run* [q]
          (fresh [t f c b d a ft]
                 (top t)
                 (fit ft)
                 (colour c)
                 (bottom b)
                 (dress d)
                 (accessory a)
                 (footwear f)
                 (== q {:trend "workshop" :top t :fit f :colour c :bottom b :dress d :accessory a :footwear f :rating 1}))))
)


(defn get-festival-trends []
  (println "Festival  Trends :->")
  (with-db festival_trend []
    (run* [q]
          (fresh [t f c b a pnt ft]
                 (top t)
                 (fit ft)
                 (colour c)
                 (bottom b)
                 (prints pnt)
                 (accessory a)
                 (footwear f)
                 (== q {:trend "festival" :top t :fit f :colour c :bottom b :prints pnt :accessory a :footwear f :rating 1}))))
  )
