import axios from 'axios';


class Youfree {

	client = axios.create({
		// baseURL: '/api/v1'
		baseURL: 'https://youfree.patrickubelhor.com/api/v1'
	});


	createUser = (email, username, password) => {
		return this.client.post('/user', {
			email: email,
			username: username,
			password: password
		});
	}


	login = (email, password) => {
		return this.client.get('/login', {
			headers: {
				email: email,
				password: password
			}
		});
	};


	logout = (token) => {
		return this.client.get('/logout', {
			headers: {
				token: token
			}
		});
	};


	addInterval = (token, dayOfWeek, startMin, endMin) => {
		return this.client.put('/schedule', {
			dayOfWeek: dayOfWeek,
			startMin: startMin,
			endMin: endMin
		}, {
			headers: {
				token: token
			}
		});
	}


	getSchedule = (token, userId) => {
		return this.client.get(`/schedule/${userId}`, {
			headers: {
				token: token
			}
		});
	};


	getFriendSchedules = (token) => {
		return this.client.get('/schedule/friends', {
			headers: {
				token: token
			}
		});
	};


	addFriend = (token, userId) => {
		return this.client.put(`/friends/${userId}`, {}, {
			headers: {
				token: token
			}
		});
	}

}

export default new Youfree();
