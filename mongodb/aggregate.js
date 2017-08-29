//aggregate tick by symbol and time 
db.results.remove({})
db.Tick.aggregate([
    //{$limit:5000},
    //refine fields
    {
        $project: {
            _id: 0,
            type: 1,
            symbol: 1,
            start: 1,
            hour: {$hour: {$add: ["$start", 28800000]}},
            minute: {$minute: {$add: ["$start", 28800000]}},
            open: 1,
            close: 1,
            high: 1,
            low: 1,
            volume: 1,
            volumeSum: 1,
            openInterest: 1
        },
    },
    //sort before group
    {$sort: {type:1,symbol:1,start: 1}},
    {
        $group: {
            _id: {
                symbol: "$symbol",
                start: {
                    $subtract: [
                        "$start",
                        {
                            $add: [
                            //aggregate time
                            // {'$multiply': [{'$hour': '$start'}, 60*60*1000]},
                            //    {'$multiply': [{'$minute': '$start'}, 60*1000]},
                                {'$multiply': [{'$second': '$start'}, 1000]},
                                {$millisecond: '$start'},
                            ]
                        }
                    ]
                },

            },
            high: {$max: "$high"},
            open: {$first: "$open"},
            low: {$min: "$low"},
            close: {$last: "$close"},
            volume: {$sum: "$volume"},
            volumeSum: {$last: "$volumeSum"},
            openInterest: {$last: "$openInterest"},
        }
    },
    //out fields
    {
        $project: {
            _id: 0,
            //symbol: "$_id.symbol",
            // startFormat: {$dateToString: {format: "%Y-%m-%d %H:%M", date: {$add: ["$_id.start", 28800000]}}},
            date: "$_id.start",
            open: 1,
            close: 1,
            high: 1,
            low: 1,
            volume: 1,
            //volumeSum: 1,
            // openInterest: 1,
        },
    },
    {$sort: {start: 1}},
    //out target
    {$out: "results"}

    ], { "allowDiskUse" : true })
