# LinksFilter is a Bukkit plugin which filters links sent to chat.
## Description
LinksFilter can control links which users send to chat using blacklist or whitelist. Also this plugin can cut links using cutt.ly service or your own site.
## Configuration
```yaml
linkfilter:
  lang: en

  #Please, don't try to enable whitelist and blacklist together...
  whitelist: true #Enabling whitelist
  blacklist: false #Enabling blacklist

  #Enabling links cutter.
  shortlinks: false

  #MySQL configuration. It should be obviously.
  #Set it up, only if you need it
  mysql:
    host: localhost
    port: 3306
    user: db_user
    password: ''
    database: db_name
    prefix: lf_

  #LinksFilter PHP API configuration.
  #Set it up, only if you need it
  api:
    url: https://example.com/api.php #Link to api.php file
    key: none #You'll get it after installing script
shortlinks:
  ################################################################################################
  # Mode of ShortLinks module.                                                                   #
  #                                                                                              #
  # MySQL mode requires MySQL configuration and installing LinksFilter API to your web-server    #
  # (You needn't to set up API configuration with MySQL mode) and put redirect link in mysql     #
  # config of this part.                                                                         #
  #                                                                                              #
  # Cuttly mode is a mode, which cuts links. You should register on https://cutt.ly/ service     #
  #                                                                                              #
  # LinksFilter mode uses LinksFilter PHP API and needs installing and configuring API           #
  ################################################################################################
  mode: cuttly

  #Cuttly API configuration
  cuttly:
    api_key: none

  #MySQL Redirect link
  mysql:
    url: https://example.com/{id}

whitelist:
  ################################################################################################
  # Mode of WhiteLust module.                                                                    #
  #                                                                                              #
  # YML mode saves list in whitelist.yml file                                                    #
  #                                                                                              #
  # MySQL mode requires MySQL configuration.                                                     #
  #                                                                                              #
  # LinksFilter mode uses LinksFilter PHP API and needs installing and configuring API           #
  ################################################################################################
  mode: yml #yml, mysql, linkfilter

blacklist:
  ################################################################################################
  # Mode of BlackList module.                                                                    #
  #                                                                                              #
  # YML mode saves list in blacklist.yml file                                                    #
  #                                                                                              #
  # MySQL mode requires MySQL configuration.                                                     #
  #                                                                                              #
  # LinksFilter mode uses LinksFilter PHP API and needs installing and configuring API           #
  ################################################################################################
  mode: yml

debug: false #You needn't it) Joke)))
```
## Comands and permissions
/lf whitelist add <site>  —  linksfilter.whitelist.add   —  add site to whitelist;

/lf whitelist remove <site>  —  linksfilter.whitelist.remove  — remove site from whitelist;

/lf blacklist add <site>  —  linksfilter.blacklist.add   —  add site to blacklist;

/lf blacklist remove <site>  —  linksfilter.blacklist.remove  — remove site from blacklist;

/lf reload  —  linksfilter.reload  —  reload plugin.

## Examples
> /lf whitelist add *.example.com  — allows access to all subdomains of example.com (example.example.com, 1.example.com, example.com etc.);

> /lf whitelist add *.example.com  — allows only access to example.com.

## LinksFilter PHP API
You should unzip archive to your web-server and open https://example.com/linksfilter/install.php/ and follow instructions.
### Info
Sources:  https://github.com/ie23s/LinksFilter-Plugin/
Sources-WEB:  https://github.com/ie23s/LinksFilter-PHP-API/

Telegram: https://t.me/Ilya_Evtukhov

P. S. Sorry for my English))
