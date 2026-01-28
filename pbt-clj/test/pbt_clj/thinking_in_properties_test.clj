(ns pbt-clj.thinking-in-properties-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as sgen]
   [clojure.test.check.clojure-test :as tc]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [pbt-clj.thinking-in-properties :as sut]
   [pbt-clj.writing-properties :as wp]))

(defn model-biggest [coll]
  (apply max coll))

(tc/defspec biggest-test 1000
  (prop/for-all [coll (s/gen (s/coll-of int?
                                        :min-count 1))]
    (= (model-biggest coll)
       (wp/biggest coll))))

(tc/defspec last-test 1000
  (prop/for-all [coll (s/gen (s/coll-of number?))
                 known-last (s/gen number?)]
    (let [known-coll (concat coll [known-last])]
      (= known-last
         (last known-coll)))))

(defn ordered? [[a b & t :as coll]]
  (if (< (count coll) 2)
    true
    (and (<= (compare a b) 0)
         (recur (cons b t)))))

(tc/defspec sort-test 1000
  (prop/for-all [coll (gen/let [t (s/gen #{number?
                                           string?
                                           char?
                                           boolean?
                                           keyword?
                                           symbol?})]
                        (s/gen (s/coll-of t)))]
    (ordered? (sort coll))))

(tc/defspec sort-same-length-test 1000
  (prop/for-all [coll (s/gen (s/coll-of number?))]
    ;; Sorted collection has the same length as the original
    (= (count coll)
       (count (sort coll)))))

(tc/defspec sort-no-added-test 1000
  (prop/for-all [coll (s/gen (s/coll-of number?))]
    ;; No elements are added during sort
    (let [sorted-coll (sort coll)]
      (every? (set coll) sorted-coll))))

(tc/defspec sort-no-removed-test 1000
  (prop/for-all [coll (s/gen (s/coll-of number?))]
    ;; No elements are removed during sort
    (let [sorted-coll (sort coll)]
      (every? (set sorted-coll) coll))))

(tc/defspec encode-decode-symmetric-test 1000
  (prop/for-all [x gen/any-printable-equatable]
    ;; Encoding then decoding returns the original value
    (let [encoded (sut/encode x)]
      (and (string? encoded)
           (= x
              (sut/decode encoded))))))
