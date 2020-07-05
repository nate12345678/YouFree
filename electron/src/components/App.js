import '../css//App.css';
import React from 'react';
import youfree from '../api/Youfree';
import Authentication from './login/Authentication';
import Dashboard from './Dashboard';
import Header from './Header';
import {
	BrowserRouter as Router,
	Route,
	Switch
} from 'react-router-dom';
import SearchPage from './search/SearchPage';

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


	componentDidMount() {
		// Auto-login
		const token = localStorage.getItem('token');
		const userId = +localStorage.getItem('userId');
		if (token && userId) {
			this.setState({
				token: token,
				userId: userId
			});
		}
	}


	getDashboard = async () => {
		// TODO: can do these simultaneously
		await this.getSchedule(this.state.userId);
		await this.getFriendSchedules();
	};


	createUser = async (email, username, password, remember) => {
		try {
			const createUserResponse = await youfree.post('/user', {
				email: email,
				username: username,
				password: password
			});

			const token = createUserResponse.headers.token;
			const user = createUserResponse.data;

			if (remember) {
				localStorage.setItem('token', token);
				localStorage.setItem('userId', user.id);
			}

			this.setState({
				token: token,
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
		}
	};


	login = async (email, password, remember) => {
		try {
			const loginReq = await youfree.get('/login', {
				headers: {
					email: email,
					password: password
				}
			});

			const user = loginReq.data;
			const token = loginReq.headers.token;

			if (remember) {
				localStorage.setItem('token', token);
				localStorage.setItem('userId', user.id);
			}

			this.setState({
				token: token,
				userId: user.id
			});
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
		}
	};


	logout = async () => {
		try {
			await youfree.get('/logout', {
				headers: {
					token: this.state.token
				}
			});

			localStorage.clear();
			this.setState({
				token: null,
				userId: null
			})
			console.log('Logged out');
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
		}
	}


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


	addFriend = async (userId) => {
		try {
			await youfree.put(`/friends/${userId}`, {}, {
				headers: {
					token: this.state.token
				}
			});

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
	}


	render() {

		let content;
		if (this.state.token == null) {
			content = <Authentication onLoginSubmit={this.login}
			                       onCreateUserSubmit={this.createUser}/>;
		} else {
			content = (
				<Switch>
					<Route path="/search">
						<SearchPage token={this.state.token} addFriend={this.addFriend} />
					</Route>
					<Route path="/">
						<Dashboard getDashboard={this.getDashboard}
						           schedule={this.state.schedule}
						           friends={this.state.friendSchedules}
						           onAddInterval={this.addInterval}/>
					</Route>
				</Switch>
			);
		}

		return (
			<Router>
				<Header logout={this.logout} />
				<div id="content">
					{content}
				</div>
			</Router>
		);
	}
}

export default App;
