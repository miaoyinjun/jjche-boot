```sh
	
#    server {
#        listen 80;
#        listen 443;
#        server_name ta-qa.wuxidiagnostics.com;
#        rewrite ^(.*)$ https://${server_name}$1 permanent;
#    }

upstream load_balance_server {
    #weigth参数表示权值，权值越高被分配到的几率越大
    server 1.1.1.1:8801   weight=5;
    server 2.2.2.2:8801   weight=5;
}

    server {
        listen 8443 default ssl;
        server_name  localhost;

        ssl_certificate_key cert/2020wxdx-private.key;
        ssl_certificate cert/2020wxdx-ssl.cert;
        
        ssl_session_timeout 5m;
        ssl_protocols TLSv1.1 TLSv1.2;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
        ssl_prefer_server_ciphers on;
	    client_max_body_size 50M;
        client_body_buffer_size 30M;


        gzip  on;
	    gzip_vary on;
        gzip_min_length 1k;
        gzip_buffers 4 16k;
        gzip_comp_level 6;
	    gzip_types text/plain application/javascript application/x-javascript text/javascript text/css application/xml application/json image/jpeg image/gif image/png image/x-icon;

        location / {
          root   /etc/nginx/ui;
          index  index.html index.htm;
          try_files $uri $uri/ /index.html;

	      if ($request_filename ~* ^.*?.(html|htm)$){
        	add_header Cache-Control no-cache,no-store,must-revalidate;
    	   }
        }

	    location ~* /(api) {
        proxy_pass http://load_balance_server;
	   
	    proxy_buffering on;
	    proxy_buffers 32 1280k;
	    proxy_buffer_size 1280k;
	    proxy_busy_buffers_size 1280k;

        proxy_connect_timeout 15s;
        proxy_send_timeout 15s;
        proxy_read_timeout 15s;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;   
	    proxy_set_header        Host            $http_host;
        add_header Content-Security-Policy upgrade-insecure-requests;
	    add_header Access-Control-Allow-Origin *;
	    proxy_set_header Cookie $http_cookie;
	    proxy_set_header X-Forwarded-Proto https;

    }
		
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
```

