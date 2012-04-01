(ns kedai.server
  (:require kedai.core)
  (:gen-class)
  (:import
   (org.jboss.netty.bootstrap ServerBootstrap)
   (org.jboss.netty.buffer ChannelBuffer)
   (org.jboss.netty.channel ChannelFactory ChannelFutureListener ChannelHandler
                            Channels MessageEvent SimpleChannelHandler)
   (org.jboss.netty.channel.socket.nio NioServerSocketChannelFactory)
   (org.jboss.netty.handler.codec.frame DelimiterBasedFrameDecoder Delimiters)
   (org.jboss.netty.handler.codec.string StringDecoder StringEncoder)
   (org.jboss.netty.util CharsetUtil)
   (java.net InetSocketAddress)
   (java.util.concurrent Executors)))

(def server-port 4242)

(defn parse-command
  "Parse a command received by the server.

   This method qualifies all symbols to be in the `kedai.core` namespace. If any
   symbol does not exist in the `kedai.core` namespace, it is left alone."
  [command]

  (map #(if (symbol? %)
          (let [resolved (ns-resolve 'kedai.core %)]
            (if (nil? resolved) % resolved))
          %)
       (read-string command)))

(defn run-command
  "Run a parsed command."
  [parsed-command]

  (str (eval parsed-command)))

(defn make-server-handler
  "Generate a server handler."
  []

  (proxy [SimpleChannelHandler] []
    (messageReceived [ctx e]
      (let [channel (.getChannel e)
            message (-> e .getMessage)

            ;; Parse the command, execute, and obtain a response.
            parsed-command (parse-command message)
            response (str (run-command parsed-command) "\n")]
        (.write channel response)))

    (exceptionCaught [ctx e]
      (-> e .getCause .printStackTrace)
      (-> e .getChannel .close))))

(defn -main
  "Start the Netty server."
  [args]

  (println "here")

  (let [;; The channel factory creates and manages Channel objects and their
        ;; related resources.
        factory (NioServerSocketChannelFactory.
                 (Executors/newCachedThreadPool)
                 (Executors/newCachedThreadPool))

        ;; Helper class used to set up the server.
        bootstrap (ServerBootstrap. factory)
        pipeline (.getPipeline bootstrap)]

    (doto pipeline
      (.addLast "framer" (DelimiterBasedFrameDecoder.
                                8192 (Delimiters/lineDelimiter)))
      (.addLast "decoder" (StringDecoder. CharsetUtil/UTF_8))
      (.addLast "encoder" (StringEncoder. CharsetUtil/UTF_8))

      (.addLast "handler" (make-server-handler)))

    (doto bootstrap
      (.setOption "child.tcpNoDelay" true)
      (.setOption "child.keepAlive" true)
      (.bind (InetSocketAddress. server-port)))

    pipeline))
