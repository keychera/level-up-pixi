# level-up with pixi.js + shadow cljs

need [shadow-cljs](https://github.com/thheller/shadow-cljs)

## quick run
```sh
npx shadow-cljs watch frontend
```

## production

locally (need [babashka](https://book.babashka.org/#_installation))
```sh
npx shadow-cljs release frontend
bb serve.clj --dir public
```

deploy to github-pages
```sh
bb deploy.clj
```

## todo

    ☑ do pixi tutorial, rotating bunny
    ☑ tried out some animation flows
    ☑ interacting with mouse events
    ☐ deal with canvas screen size (mobile first)
    ☐ layout
    ☐ button and keyboards
    ☐ progress bar
    ☐ exp assets
    ☐ level up modal
    ☐ research particles, animations
    ☑ deployment
