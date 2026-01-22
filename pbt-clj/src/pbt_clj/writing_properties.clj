(ns pbt-clj.writing-properties)

(defn- biggest* [[head & tail :as xs] max]
  (cond
    (empty? xs) max
    (>= head max) (recur tail head)
    :else (recur tail max)))

(defn biggest [[head & tail]]
  (biggest* tail head))
