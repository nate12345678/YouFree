import '../css//App.css';
import React from 'react';
import youfree from '../api/Youfree';
import Authentication from './login/Authentication';
import Dashboard from './Dashboard';
import Header from './Header';

class App extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			token: null,
			userId: null,
			schedule: null,
			friends: null,
			friendSchedules: null
		};
	}


	getDashboard = async () => {
		// TODO: can do these simultaneously
		await this.getSchedule(this.state.userId);
		await this.getFriendSchedules();
	};


	createUser = async (email, username, password) => {
		let user = null;
		try {
			const createUserResponse = await youfree.post('/user', {
				email: email,
				username: username,
				password: password
			});

			user = createUserResponse.data;

			this.setState({
				token: createUserResponse.headers.token,
				userId: user.id
			});

			console.log('Created new user');
		} catch (error) {
			if (error.response !== undefined) {
				console.log(error.response);
				// TODO: pop up with error message
				return;
			}

			console.log('An unknown error has occurred');
			// TODO: pop up with error message
			return;
		}
	};


	login = async (email, password) => {
		let token = null;
		let user = null;
		try {
			const loginReq = await youfree.get('/login', {
				headers: {
					email: email,
					password: password
				}
			});

			user = loginReq.data;
			token = loginReq.headers.token;
			console.log('Logged in');
		} catch (error) {
			if (error.response !== undefined) {
				console.log(error.response);
				// TODO: pop up with error message
				// TODO: indicate user was created but couldn't log in
				return;
			}

			console.log('An unknown error has occurred');
			// TODO: pop up with error message
			// TODO: indicate user was created but couldn't log in
			return;
		}

		this.setState(() => ({
			token: token,
			userId: user.id
		}));
	};


	addInterval = async (dayOfWeek, startMin, endMin) => {
		let schedule = null;
		try {
			const addIntervalReq = await youfree.put('/schedule', {
				dayOfWeek: dayOfWeek,
				startMin: startMin,
				endMin: endMin
			}, {
				headers: {
					token: this.state.token
				}
			});

			console.log('Added interval');
			schedule = addIntervalReq.data;
		} catch (error) {
			if (error.response !== undefined) {
				console.log(error.response);
				// TODO: pop up with error message
				return;
			}

			console.log('An unknown error has occurred');
			// TODO: pop up with error message
			return;
		}

		this.setState(() => ({
			schedule: schedule
		}));
	};


	getSchedule = async (userId) => {
		let schedule = null;
		try {
			const getScheduleResponse = await youfree.get(`/schedule/${userId}`, {
				headers: {
					token: this.state.token
				}
			});
			schedule = getScheduleResponse.data;

		} catch (error) {
			if (error.response !== undefined) {
				console.log(error.response);
				// TODO: pop up with error message
				return;
			}

			console.log('An unknown error has occurred');
			// TODO: pop up with error message
			return;
		}

		this.setState(() => ({
			schedule: schedule
		}));
	};


	getFriends = async () => {
		let friends = null;

		try {
			const getFriendsResponse = await youfree.get('/friends', {
				headers: {
					token: this.state.token
				}
			});

			friends = getFriendsResponse.data;
		} catch (error) {
			if (error.response !== undefined) {
				console.log(error.response);
				// TODO: pop up with error message
				return;
			}

			console.log('An unknown error has occurred');
			// TODO: pop up with error message
			return;
		}

		this.setState(() => ({
			friends: friends
		}));
	}


	getFriendSchedules = async () => {
		let friendSchedules = null;

		try {
			const getFriendSchedulesResponse = await youfree.get('/schedule/friends', {
				headers: {
					token: this.state.token
				}
			});

			friendSchedules = getFriendSchedulesResponse.data;
		} catch (error) {
			if (error.response !== undefined) {
				console.log(error.response);
				// TODO: pop up with error message
				return;
			}

			console.log('An unknown error has occurred');
			// TODO: pop up with error message
			return;
		}

		this.setState(() => ({
			friendSchedules: friendSchedules
		}));
	}


	render() {
		let content;
		if (this.state.token == null) {
			content = <Authentication onLoginSubmit={this.login} onCreateUserSubmit={this.createUser}/>;
		} else {
			content = <Dashboard getDashboard={this.getDashboard} schedule={this.state.schedule} friends={this.state.friendSchedules} onEdit={this.addInterval}/>;
		}

		return (
			<div>
				<Header/>
				<div id="content">
					{content}
				</div>
			</div>
		);
	}
}

export default App;
