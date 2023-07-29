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
    ☑ deal with canvas screen size (mobile first)
    ☐ "level up" idea, basically a remake of level-up UI from the game Honkai: Star Rail/Genshin
        ☐ layout
        ☐ buttons
        ☐ progress bar
        ☐ 3 exp button assets
        ☐ level up modal
    ☐ research about animations, figure out the animation render loop 
        ☐ spawn an animated object
        ☑ pace/speed of an animation, slower/faster/controllable shake animation
            ☐ I think Tweedle.js can cover this, so understanding it is now the goal
        ☐ modal-like UI
        ☐ progress bar animation
    ☐ research about particles
    ☑ deployment
