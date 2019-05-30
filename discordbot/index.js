const Discord = require('discord.js')
const client = new Discord.Client()
client.on('ready', () => {
  console.log(`Logged in as ${client.user.tag}!`)
})
client.on('message', msg => {
 	if (msg.content === 'ping') {
    		msg.reply('Pong!')
  	}
	else if(msg.content === '//hello'){
		hello(msg)
	}
	else if(msg.content === '//help'){
		msg.author.send("//snowflake : Returns your snowflake id")
		msg.author.send("//schedule : display if and when friend's are avaliable")
	}
	else if(msg.content === '//snowflake'){
		getSnowflake(msg,function(snowflake){
			msg.author.send(snowflake);
		});
	}
	else if(msg.content === '//guild'){
		getGuild(msg,function(guild){
			msg.reply(guild);
		});
	}
	else if(msg.content === '//schedule'){
		listSchedule(msg);
	}
})
client.login('NTQ4NzEwMTM2MDM4OTQ4ODg0.D1JSJw.riQJrP5kEGVTScHuhlIlRTCAUK8')

function hello(msg){
	console.log("hello");
	var test = msg.guild.members;
	var filtered = [];
	for(let [snowflake] of test){
		
	}
}


function listSchedule(msg){
	var request = require('request');
        var options = {url:'http://216.171.4.52:8080/api/v1/friends/discord',headers: {'snowflake':msg.author.id}};
        request(options, function(err, response, body) {
        if(err) { console.log(err); return; }
	//console.log(body);
	
	var friendlist = body;
	
	var guildList = msg.guild.members;
        var c = [];
	var i = 0;
        for(let [snowflake,guildMember] of guildList){
		console.log(guildMember.user.username);
		if(friendlist.indexOf(snowflake) != -1)
			c[i++] = snowflake;
	}
	//console.log(c);
	var now = new Date()
	var flag = 0;
	for(var user of c){
		var request = require('request');
        	request('http://216.171.4.52:8080/api/v1/schedule/discord/' + user, function(err, response, body,flag) {
        		if(err) { console.log(err); return; }
			
			console.log(body);
			
			console.log(now.getDay());
			console.log(now.getHours());
			console.log(now.getMinutes());
			var day = now.getDay() -1;
			if(day == -1)
				day = 6;
			body = body.replace(/[^0-9]/g, '');
			console.log(body);
//			console.log(body[95* day + (now.getHours() + Math.round(now.getMinutes() / 15))]);
//			console.log(95* day + (now.getHours() + Math.round(now.getMinutes() / 15)));
//			console.log((now.getHours() + Math.round(now.getMinutes() / 15)));


			if(body[95* day + (4*now.getHours() + Math.round(now.getMinutes() / 15))]==='0'){
				for(let [snowflake,guildMember] of guildList){
					if(snowflake === user){
						msg.channel.send(guildMember.user.username + " is currently free!");
						flag = 1;
					}
				}
			}
			//var looped = 0
			//else{
			//	while(!looped){
			//		for(i = now.getDay(); i < 7;i++){
			//			for(j = (now.getHours() + Math.round(now.getMinutes() / 15)); j < 96; j++){
			//				if(body[96* now.getDay() + (now.getHours() + Math.round(now.getMinutes() / 15))]==='0'){
			//					msg.reply(
			//	}
			//}
			//for(let [snowflake,guildMember] of guildList){
                                  //      if(snowflake === user)
                                //                msg.reply(guildMember.user.username + " is currently free!");
                              //  }

		});
	}
	//if(flag == 0)
	//	msg.channel.send("No one is currently free!");
	});
}

function getSnowflake(msg,snowflake){
	snowflake(msg.author.id)
}

function getGuild(msg,guild){
	guild(msg.guild.id)
}


