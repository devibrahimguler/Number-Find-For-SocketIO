const http = require('http').createServer();
const io = require('socket.io')(http);

http.listen(1304);

const mySql = require('mysql');
const { reset } = require('nodemon');
const { setInterval } = require('timers');
const connection = mySql.createConnection
    ({
        "host": "localhost",
        "user": "root",
        "password": "",
        "database": "sayi-bulma"
    });

connection.connect(() => {

});

let users = [];
let totalHome = [];
let totalAway = [];
let homeAnswers = [];
let awayAnswers = [];
let games = [];
let sendHomeGames = [];
let sendAwayGames = [];
let allTrue = [];
let onlyNumber = [];
let allFalse = [];
let clickhomes = [];
let clickaways = [];
let selectedhomes = [];
let selectedaways = [];
let homeCevaps = [];
let awayCevaps = [];
let leaders = [];

io.on('connection', socket => {
    socket.on('user_connect', (data) => {
        connection.query(`SELECT id FROM kullanici WHERE isim = '${data.isim}'`, (error, user) => {
            if (user.length > 0) {
                connection.query(`SELECT id FROM kullanici WHERE isim = '${data.isim}' AND sifre = '${data.sifre}'`, (error, user) => {
                    if (user.length > 0) {
                        let userObj = { id: socket.id, isim: data.isim, durum: true };
                        users.push(userObj);
                        io.to(socket.id).emit('user_connect', data.isim);
                    }
                    else {
                        io.to(socket.id).emit('wrong_password', 'Şifre Yanlış !');
                    }

                });
            }
            else if (data.isim !== '') {
                connection.query(`INSERT INTO kullanici (isim,sifre) VALUES ('${data.isim}','${data.sifre}')`);
                let userObj = { id: socket.id, isim: data.isim, durum: true };
                users.push(userObj);

                io.to(socket.id).emit('user_connect', data.isim);

            }
        });
    });

    socket.on('online_users', () => {
        io.emit('online_users', users);
    });

    socket.on('leaderboard', () =>{
        connection.query("SELECT isim, puan FROM kullanici ORDER BY puan DESC", (error, users) => {
            
            for(var i = 0; i < users.length; i++){
                let obj = {
                    sira : i + 1,
                    isim : users[i].isim,
                    puan : users[i].puan
                }
                leaders.push(obj);
            }
            io.to(socket.id).emit('leaders', leaders);
            leaders = [];
        });
    });

    socket.on('disconnect', () => {
        users = users.filter((user) => {
            totalHome = [];
            totalAway = [];
            homeAnswers = [];
            awayAnswers = [];
            games = [];
            sendHomeGames = [];
            sendAwayGames = [];
            allTrue = [];
            onlyNumber = [];
            allFalse = [];
            clickhomes = [];
            clickaways = [];
            selectedhomes = [];
            selectedaways = [];
            homeCevaps = [];
            awayCevaps = [];
            return user.id !== socket.id;
        });
        games = games.filter((game) => {
            totalHome = [];
            totalAway = [];
            homeAnswers = [];
            awayAnswers = [];
            games = [];
            sendHomeGames = [];
            sendAwayGames = [];
            allTrue = [];
            onlyNumber = [];
            allFalse = [];
            clickhomes = [];
            clickaways = [];
            selectedhomes = [];
            selectedaways = [];
            homeCevaps = [];
            awayCevaps = [];
            return game.home.id !== socket.id && game.away.id !== socket.id;
        });
        io.emit('online_users', users);
    });

    socket.on('request', (id) => {
        let user = users.find(user => user.id === socket.id);
        io.to(id).emit('request', user);
    });

    socket.on('accept', (id) => {
        let homePlayer = users.find(user => user.id == id);
        let awayPlayer = users.find(user => user.id == socket.id);
        let joinPlayer = { home: homePlayer.isim, away: awayPlayer.isim };
        io.to(socket.id).emit('accept', joinPlayer);
        io.to(id).emit('accept', joinPlayer);
        homePlayer.durum = false;
        awayPlayer.durum = false;
        io.emit('online_users', users);

        let game = {
            home: homePlayer,
            away: awayPlayer,
            time: 30,
            emit: false
        };

        let sendHomeGame = {
            home: homePlayer,
            away: awayPlayer,
            sendtimehome: 30,
            emit: false
        };

        let sendAwayGame = {
            home: homePlayer,
            away: awayPlayer,
            sendtimeaway: 30,
            emit: false
        };

        sendAwayGames.push(sendAwayGame);
        sendHomeGames.push(sendHomeGame);
        games.push(game);

    });

    socket.on('reject', (id) => {
        let user = users.find(user => user.id === socket.id);
        io.to(id).emit('reject', user.isim);
    });

    socket.on('game', (players) => {
        let game = games.find(x => x.home.isim === players.home || x.away.isim === players.away);
        io.to(game.home.id).emit('game', game);
        io.to(game.away.id).emit('game', game);
        if (game.emit === false) {
            let timer = setInterval(() => {
                if (game.time > 0) {
                    game.time--;
                    io.to(game.home.id).emit('time', game.time);
                    io.to(game.away.id).emit('time', game.time);
                    if (selectedhomes[0] === 'selectedhome' && selectedaways[0] === 'selectedaway') {
                        game.time = 0;
                        selectedhomes = [];
                        selectedaways = [];
                    }

                }
                else if (game.time === 0 && homeAnswers.length !== 0 && awayAnswers.length !== 0) {
                    io.to(game.home.id).emit('selected', homeAnswers);
                    io.to(game.away.id).emit('selected', awayAnswers);

                    clearInterval(timer);
                    let sendHomeGame = sendHomeGames.find(x => x.home.isim === players.home || x.away.isim === players.away);

                    sendHomeGames.push(sendHomeGame);
                    io.to(sendHomeGame.home.id).emit('sendhomegame', sendHomeGame);
                    io.to(sendHomeGame.away.id).emit('sendhomegame', sendHomeGame);
                    io.to(sendHomeGame.home.id).emit('enableclickhome');
                    io.to(sendHomeGame.away.id).emit('notenableclickaway');
                    let sendHomeTimer = setInterval(callbackHome, 1000);
                    function callbackHome() {

                        if (sendHomeGame.sendtimehome > 0) {
                            sendHomeGame.sendtimehome--;
                            io.to(sendHomeGame.home.id).emit('sendtimehome', sendHomeGame.sendtimehome);
                            if (clickhomes[0] === 'clickhome') {
                                sendHomeGame.sendtimehome = 0;
                                clickhomes = [];
                            }

                        }
                        else if (sendHomeGame.sendtimehome === 0) {
                            clearInterval(sendHomeTimer);


                            let sendHomeGame = sendHomeGames.find(x => x.home.isim === players.home || x.away.isim === players.away);

                            let add = `${allTrue.length} tanesinin yeri ve numarası doğru, ${onlyNumber.length} numara doğru, ${allFalse.length} numara yanlış'`;

                            let homeCevap1 = totalHome[0];
                            let homeCevap2 = totalHome[1]; 
                            let homeCevap3 = totalHome[2]; 
                            let homeCevap4 = totalHome[3];  
                            let cevapHome = {
                                cevap1 : homeCevap1,
                                cevap2 : homeCevap2,
                                cevap3 : homeCevap3,
                                cevap4 : homeCevap4,
                                allcevap : add
                            }
                            homeCevaps.push(cevapHome);
                            io.to(sendHomeGame.home.id).emit('sendtimehome', sendHomeGame.sendtimehome);
                            io.to(sendHomeGame.home.id).emit('totalanswer', homeCevaps);

                            allTrue = [];
                            onlyNumber = [];
                            allFalse = [];



                            if (awayAnswers[0] == totalHome[0] && awayAnswers[1] == totalHome[1] && awayAnswers[2] == totalHome[2] && awayAnswers[3] == totalHome[3]) {
                                let sendHomeGame = sendHomeGames.find(x => x.home.isim === players.home || x.away.isim === players.away);

                                let homeAnswer = homeAnswers[0]
                                let homeAnswer1 = homeAnswers[1]
                                let homeAnswer2 = homeAnswers[2]
                                let homeAnswer3 = homeAnswers[3]

                                let awayAnswer = awayAnswers[0]
                                let awayAnswer1 = awayAnswers[1]
                                let awayAnswer2 = awayAnswers[2]
                                let awayAnswer3 = awayAnswers[3]

                                let homePlayer = sendHomeGame.home.isim;
                                let awayPlayer = sendHomeGame.away.isim;
                                let result = `Oyunu ${homePlayer} kazandı!`;
                                let data = { homePlayer, awayPlayer, homeAnswer, homeAnswer1, homeAnswer2, homeAnswer3, awayAnswer, awayAnswer1, awayAnswer2, awayAnswer3, result };
                                connection.query(`UPDATE kullanici SET puan = puan + 1 WHERE isim = '${homePlayer}'`);
                                clearInterval(sendHomeTimer);

                                io.to(sendHomeGame.home.id).emit('finish', data);
                                io.to(sendHomeGame.away.id).emit('finish', data);

                                let homeUser = users.find(user => user.isim === homePlayer);
                                let awayUser = users.find(user => user.isim === awayPlayer);
                                homeUser.durum = true;
                                awayUser.durum = true;
                                io.emit('online_users', users);

                                totalHome = [];
                                totalAway = [];
                                homeAnswers = [];
                                awayAnswers = [];
                                games = [];
                                sendHomeGames = [];
                                sendAwayGames = [];
                                allTrue = [];
                                onlyNumber = [];
                                allFalse = [];

                            } else {
                                clearInterval(sendHomeTimer);
                                totalHome = [];

                                let sendAwayGame = sendAwayGames.find(x => x.home.isim === players.home || x.away.isim === players.away);

                                if (sendAwayGame.sendtimeaway === 0) {
                                    sendAwayGames.map(x => x.sendtimeaway = 30);
                                }
                                
                                io.to(sendAwayGame.home.id).emit('sendawaygame', sendAwayGame);
                                io.to(sendAwayGame.away.id).emit('sendawaygame', sendAwayGame);
                                io.to(sendAwayGame.away.id).emit('enableclickaway');
                                io.to(sendAwayGame.home.id).emit('notenableclickhome');
                                let sendAwayTimer = setInterval(callbackAway, 1000);
                                function callbackAway() {
                                    if (sendAwayGame.sendtimeaway > 0) {
                                        sendAwayGame.sendtimeaway--;
                                        io.to(sendAwayGame.away.id).emit('sendtimehome', sendAwayGame.sendtimeaway);

                                        if (clickaways[0] === 'clickaway') {
                                            sendAwayGame.sendtimeaway = 0;
                                            clickaways = [];
                                        }

                                    }
                                    else if (sendAwayGame.sendtimeaway === 0) {
                                        if (homeAnswers[0] == totalAway[0] && homeAnswers[1] == totalAway[1] && homeAnswers[2] == totalAway[2] && homeAnswers[3] == totalAway[3]) {
                                            let sendAwayGame = sendAwayGames.find(x => x.home.isim === players.home || x.away.isim === players.away);

                                            let homeAnswer = homeAnswers[0]
                                            let homeAnswer1 = homeAnswers[1]
                                            let homeAnswer2 = homeAnswers[2]
                                            let homeAnswer3 = homeAnswers[3]

                                            let awayAnswer = awayAnswers[0]
                                            let awayAnswer1 = awayAnswers[1]
                                            let awayAnswer2 = awayAnswers[2]
                                            let awayAnswer3 = awayAnswers[3]

                                            let homePlayer = sendAwayGame.home.isim;
                                            let awayPlayer = sendAwayGame.away.isim;
                                            let result = `Oyunu ${awayPlayer} kazandı!`;
                                            connection.query(`UPDATE kullanici SET puan = puan + 1 WHERE isim = '${awayPlayer}'`);
                                            let data = { homePlayer, awayPlayer, homeAnswer, homeAnswer1, homeAnswer2, homeAnswer3, awayAnswer, awayAnswer1, awayAnswer2, awayAnswer3, result };

                                            clearInterval(sendAwayTimer);

                                            io.to(sendAwayGame.home.id).emit('finish', data);
                                            io.to(sendAwayGame.away.id).emit('finish', data);

                                            let homeUser = users.find(user => user.isim === homePlayer);
                                            let awayUser = users.find(user => user.isim === awayPlayer);
                                            homeUser.durum = true;
                                            awayUser.durum = true;
                                            io.emit('online_users', users);


                                            totalHome = [];
                                            totalAway = [];
                                            homeAnswers = [];
                                            awayAnswers = [];
                                            games = [];
                                            sendHomeGames = [];
                                            sendAwayGames = [];
                                            allTrue = [];
                                            onlyNumber = [];
                                            allFalse = [];
                                            homeCevaps = [];
                                            awayCevaps = [];
                                        } else {
                                            clearInterval(sendAwayTimer);

                                            let sendAwayGame = sendAwayGames.find(x => x.home.isim === players.home || x.away.isim === players.away);

                                            let add = `${allTrue.length} tanesinin yeri ve numarası doğru, ${onlyNumber.length} numara doğru, ${allFalse.length} numara yanlış'`;

                                            let awayCevap1 = totalAway[0];
                                            let awayCevap2 = totalAway[1];
                                            let awayCevap3 = totalAway[2];
                                            let awayCevap4 = totalAway[3];
                                            let cevapAway = {
                                                cevap1 : awayCevap1,
                                                cevap2 : awayCevap2,
                                                cevap3 : awayCevap3,
                                                cevap4 : awayCevap4,
                                                allcevap : add
                                            }
                                            awayCevaps.push(cevapAway);

                                            io.to(sendAwayGame.away.id).emit('sendtimehome', sendAwayGame.sendtimeaway);
                                            io.to(sendAwayGame.away.id).emit('totalanswer', awayCevaps);
                                            sendHomeGames.map(x => x.sendtimehome = 30);
                                            io.to(sendHomeGame.home.id).emit('enableclickhome');
                                            io.to(sendHomeGame.away.id).emit('notenableclickaway');

                                            allTrue = [];
                                            onlyNumber = [];
                                            allFalse = [];
                                            totalAway = [];

                                            sendHomeTimer = setInterval(callbackHome, 1000);
                                        }

                                    }
                                }
                            }
                        }
                    }
                } else if (game.time === 0 && homeAnswers.length === 0 && awayAnswers.length === 0) {
                    let homePlayer = game.home.isim;
                    let awayPlayer = game.away.isim;
                    io.to(game.home.id).emit('not_selected');
                    io.to(game.away.id).emit('not_selected');

                    let homeUser = users.find(user => user.isim === homePlayer);
                    let awayUser = users.find(user => user.isim === awayPlayer);
                    homeUser.durum = true;
                    awayUser.durum = true;
                    io.emit('online_users', users);

                    totalHome = [];
                    totalAway = [];
                    homeAnswers = [];
                    awayAnswers = [];
                    games = [];
                    sendHomeGames = [];
                    sendAwayGames = [];
                    allTrue = [];
                    onlyNumber = [];
                    allFalse = [];
                    homeCevaps = [];
                    awayCevaps = [];
                    clearInterval(timer);
                }
            }, 1000);
            game.emit = true;
        }

    });

    socket.on('clickhome', (clickhome) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            clickhomes.push(clickhome);
        }
    });

    socket.on('clickaway', (clickaway) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.away.id) {
            clickaways.push(clickaway);
        }
    });

    socket.on('selectedhome', (selectedhome) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            selectedhomes.push(selectedhome);
        }
    });

    socket.on('selectedaway', (selectedaway) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.away.id) {
            selectedaways.push(selectedaway);
        }
    });

    socket.on('my_number_1', (sayi1) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            homeAnswers.push(sayi1);
        }
        else if (socket.id === game.away.id) {
            awayAnswers.push(sayi1);
        }

    });

    socket.on('my_number_2', (sayi2) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            homeAnswers.push(sayi2);
        }
        else if (socket.id === game.away.id) {
            awayAnswers.push(sayi2);
        }

    });

    socket.on('my_number_3', (sayi3) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            homeAnswers.push(sayi3);
        }
        else if (socket.id === game.away.id) {
            awayAnswers.push(sayi3);
        }

    });

    socket.on('my_number_4', (sayi4) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            homeAnswers.push(sayi4);
        }
        else if (socket.id === game.away.id) {
            awayAnswers.push(sayi4);
        }

    });

    socket.on('answer_1', (answer) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {

            if (answer === awayAnswers[0]) {
                totalHome.push(answer);
                allTrue.push(answer);
            }
            else if (answer === awayAnswers[1]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[2]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[3]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else {
                totalHome.push(answer);
                allFalse.push(answer);
            }
        }
        else if (socket.id === game.away.id) {
            if (answer === homeAnswers[0]) {
                totalAway.push(answer);
                allTrue.push(answer);
            }
            else if (answer === homeAnswers[1]) {
                totalAway.push(answer);
                onlyNumber.push(answer); 
            }
            else if (answer === homeAnswers[2]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[3]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else {
                totalAway.push(answer);
                allFalse.push(answer);
            }
        }

    });

    socket.on('answer_2', (answer) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);
        cevap = {
            cevap2 : answer
        }
        if (socket.id === game.home.id) {
            if (answer === awayAnswers[0]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[1]) {
                totalHome.push(answer);
                allTrue.push(answer);
            }
            else if (answer === awayAnswers[2]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[3]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else {
                totalHome.push(answer);
                allFalse.push(answer);
            }
        }
        else if (socket.id === game.away.id) {
            if (answer === homeAnswers[0]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[1]) {
                totalAway.push(answer);
                allTrue.push(answer);
            }
            else if (answer === homeAnswers[2]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[3]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else {
                totalAway.push(answer);
                allFalse.push(answer);
            }
        }

    });

    socket.on('answer_3', (answer) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            if (answer === awayAnswers[0]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[1]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[2]) {
                totalHome.push(answer);
                allTrue.push(answer);
            }
            else if (answer === awayAnswers[3]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else {
                totalHome.push(answer);
                allFalse.push(answer);
            }
        }
        else if (socket.id === game.away.id) {
            if (answer === homeAnswers[0]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[1]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[2]) {
                totalAway.push(answer);
                allTrue.push(answer);
            }
            else if (answer === homeAnswers[3]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else {
                totalAway.push(answer);
                allFalse.push(answer);
            }
        }

    });

    socket.on('answer_4', (answer) => {
        let game = games.find(x => x.home.id === socket.id || x.away.id === socket.id);

        if (socket.id === game.home.id) {
            if (answer === awayAnswers[0]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[1]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[2]) {
                totalHome.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === awayAnswers[3]) {
                totalHome.push(answer);
                allTrue.push(answer);
            }
            else {
                totalHome.push(answer);
                allFalse.push(answer);
            }
        }
        else if (socket.id === game.away.id) {
            if (answer === homeAnswers[0]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[1]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[2]) {
                totalAway.push(answer);
                onlyNumber.push(answer);
            }
            else if (answer === homeAnswers[3]) {
                totalAway.push(answer);
                allTrue.push(answer);
            }
            else {
                totalAway.push(answer);
                allFalse.push(answer);
            }
        }

    });
});