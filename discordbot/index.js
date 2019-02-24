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
	for(var user of c){
		var request = require('request');
        	request('http://216.171.4.52:8080/api/v1/schedule/discord/' + user, function(err, response, body) {
        		if(err) { console.log(err); return; }
			
			console.log(body);
			
			console.log(now.getDay());
			console.log(now.getHours());
			console.log(now.getMinutes());
			console.log(body);
			if(body[1 + now.getDay() * 194 + 1 + now.getHours * 16]==='0'){
				for(let [snowflake,guildMember] of guildList){
					if(snowflake === user)
						msg.reply(guildMember.user.username + " is currently free!");
				}
			}
			for(let [snowflake,guildMember] of guildList){
                                        if(snowflake === user)
                                                msg.reply(guildMember.user.username + " is currently free!");
                                }

		});
	}
	});
}

function getSnowflake(msg,snowflake){
	snowflake(msg.author.id)
}

function getGuild(msg,guild){
	guild(msg.guild.id)
}


