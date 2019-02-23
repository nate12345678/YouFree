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
		hello()
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
})
client.login('NTQ4NzEwMTM2MDM4OTQ4ODg0.D1JSJw.riQJrP5kEGVTScHuhlIlRTCAUK8')

function hello(){
	console.log("hello");
	const request = require('request');
request('http://216.171.4.52:8080/api/v1/hello', { json: true }, (err, res, body) => {
if (err) { return console.log(err); }
console.log(body);
//console.log(body.url);
//console.log(body.explanation);
});
}

function getSnowflake(msg,snowflake){
	snowflake(msg.author.id)
}

function getGuild(msg,guild){
	guild(msg.guild.id)
}


