(ns app.core
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            ["@mux/upchunk" :as upchunk])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(goog-define MUX_TOKEN_ID "")
(goog-define MUX_TOKEN_SECRET "")

(def picker (.getElementById js/document "picker"))

(defn- upload-url-c
  []
  (http/post "https://api.mux.com/video/v1/uploads"
             {:json-params {"cors_origin"        "*"
                            "new_asset_settings" {"playback_policy" ["public"]}}
              :headers     {"Content-Type" "application/json"}
              :basic-auth  {:username MUX_TOKEN_ID
                            :password MUX_TOKEN_SECRET}}))

(set!
 (.-onchange picker)
 (fn []
   (go (let [response (<! (upload-url-c))
             upload-url (get-in response [:body :data :url])]
         (doto (upchunk/createUpload (clj->js {"endpoint"  upload-url
                                               "file"      (-> picker
                                                               .-files
                                                               (aget 0))
                                               "chunkSize" 5120}))
           (.on "error" (fn [err] (.error js/console "Error!" err)))
           (.on "progress" (fn [progress] (.info js/console "Progress so far: " (.-detail progress) "%")))
           (.on "success" (fn [_success] (.log js/console "Wrap it up, we're done here. 👋"))))))))