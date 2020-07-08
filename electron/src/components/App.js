import '../css//App.css';
import React from 'react';
import youfree from '../api/Youfree';
import Dashboard from './Dashboard';
import Header from './Header';
import Authentication from './login/Authentication';
import People from './people/People';
import {
	BrowserRouter as Router,
	Route,
	Switch
} from 'react-router-dom';

class App extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			token: null,
			userId: null,
			schedule: null,
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


	handleError = (error) => {
		if (error.response !== undefined) {
			console.log(error.response);
			// TODO: pop up with error message
			return;
		}

		console.log('An unknown error has occurred');
		// TODO: pop up with error message
	}


	getDashboard = async () => {
		// TODO: can do these simultaneously
		await this.getSchedule(this.state.userId);
		await this.getFriendSchedules();
	};


	createUser = async (email, username, password, remember) => {
		try {
			const createUserResponse = await youfree.createUser(email, username, password);

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
			this.handleError(error);
		}
	};


	login = async (email, password, remember) => {
		try {
			const loginReq = await youfree.login(email, password);

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
			this.handleError(error);
		}
	};


	logout = async () => {
		try {
			await youfree.logout(this.state.token);

			localStorage.clear();
			this.setState({
				token: null,
				userId: null
			});
			console.log('Logged out');
		} catch (error) {
			this.handleError(error);
		}
	};


	addInterval = async (dayOfWeek, startMin, endMin) => {
		let schedule = null;
		try {
			const addIntervalReq = await youfree.addInterval(this.state.token, dayOfWeek, startMin, endMin);

			console.log('Added interval');
			schedule = addIntervalReq.data;

			this.setState({
				schedule: schedule
			});
		} catch (error) {
			this.handleError(error);
		}
	};


	getSchedule = async (userId) => {
		let schedule = null;
		try {
			const getScheduleResponse = await youfree.getSchedule(this.state.token, userId);
			schedule = getScheduleResponse.data;

			this.setState({
				schedule: schedule
			});

		} catch (error) {
			this.handleError(error);
		}
	};


	getFriendSchedules = async () => {
		try {
			const getFriendSchedulesResponse = await youfree.getFriendSchedules(this.state.token);

			this.setState({
				friendSchedules: getFriendSchedulesResponse.data
			});
		} catch (error) {
			this.handleError(error);
		}

	};


	addFriend = async (userId) => {
		try {
			await youfree.addFriend(this.state.token, userId);

		} catch (error) {
			this.handleError(error);
		}
	};


	render() {

		let content;
		if (this.state.token == null) {
			content = <Authentication onLoginSubmit={this.login}
			                          onCreateUserSubmit={this.createUser}/>;
		} else {
			content = (
				<Switch>
					<Route path="/search">
						<People token={this.state.token} addFriend={this.addFriend}/>
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
				<Header logout={this.logout}/>
				<div id="content">
					{content}
				</div>
			</Router>
		);
	}
}

export default App;
