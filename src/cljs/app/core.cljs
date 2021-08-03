(ns app.core
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(goog-define MUX_TOKEN_ID "")
(goog-define MUX_TOKEN_SECRET "")

(comment
;; curl https://api.mux.com/video/v1/uploads \
;;   -X POST \
;;   -d '{ "cors_origin": "*", "new_asset_settings": { "playback_policy": ["public"] }'
;;   -H "Content-Type: application/json" \
;;   -u ${MUX_TOKEN_ID}:${MUX_TOKEN_SECRET}  

  (go
    (let [response (<! (http/post "https://api.mux.com/video/v1/uploads"
                                  {:json-params {"cors_origin"        "*"
                                                 "new_asset_settings" {"playback_policy" ["public"]}}
                                   :headers     {"Content-Type" "application/json"}
                                   :basic-auth  {:username MUX_TOKEN_ID
                                                 :password MUX_TOKEN_SECRET}}))]
      (prn response)))
  
  )