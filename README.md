# Yubot
Yubot is a trading bot. It's still work in progress,but hopefully you can see where we're headed.
## Description

- Fully-automated [technical-analysis](http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:introduction_to_technical_indicators_and_oscillators)-based trading approach
- Plugin architecture for implementing exchange support, or writing new strategies
- Simulator for [Backtesting strategies](https://gist.github.com/carlos8f/b09a734cf626ffb9bb3bcb1ca35f3db4) against historical data
- "Paper" trading mode, operates on a simulated balance while watching the live market
- Configurable sell stops, buy stops, and (trailing) profit stops

## First Step -build historical data data module
 It will include 3 alone parts
 
 - A scalable datastore for metrics, events, and real-time analytics
 - An agent lib for collecting, processing, aggregating, and writing metrics.
 - A visualization UI for the history tick stack
