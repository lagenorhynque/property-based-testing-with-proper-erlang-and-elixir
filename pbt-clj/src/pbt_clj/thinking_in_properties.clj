(ns pbt-clj.thinking-in-properties
  (:require
   [clojure.spec.alpha :as s]))

(s/fdef encode
  :args (s/cat :x any?)
  :ret string?)

(defn encode [x]
  (pr-str x))

(s/fdef decode
  :args (s/cat :x string?)
  :ret any?)

(defn decode [x]
  (read-string x))
