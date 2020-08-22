import '../css//App.css';
import React from 'react';
import youfree from '../api/Youfree';
import Dashboard from './Dashboard';
import Header from './Header';
import AuthenticationPage from './login/AuthenticationPage';
import People from './people/People';
import {
	BrowserRouter as Router,
	Route,
	Switch
} from 'react-router-dom';
import { createMuiTheme } from '@material-ui/core';
import * as Colors from '@material-ui/core/colors';
import { ThemeProvider } from '@material-ui/core/styles';
import AboutPage from './AboutPage';
import MyProfilePage from './MyProfilePage';


const lightTheme = createMuiTheme({
	palette: {
		primary: {
			main: Colors.indigo['500'],
			dark: Colors.indigo['700']
		},
		secondary: {
			main: Colors.pink['300']
		}
	}
});

const darkTheme = createMuiTheme({
	palette: {
		type: 'dark',
		primary: {
			main: Colors.indigo['200']
		},
		secondary: {
			main: Colors.pink['800']
		}
	}
});


class App extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			currentDay: 0,
			token: null,
			self: null,
			schedule: null,
			friendSchedules: null,
			theme: 'light',
		};
	}


	componentDidMount() {
		// Auto-login
		const token = localStorage.getItem('token');
		const self = JSON.parse(localStorage.getItem('self'));
		if (token && self) {
			this.setState({
				token: token,
				self: self
			});
		}
	}


	invertTheme = () => {
		const from = this.state.theme;
		const to = this.state.theme === 'light' ? 'dark' : 'light';

		this.setState({
			theme: to
		});
		document.body.classList.replace(from, to);
	}


	handleError = (error) => {
		if (error.response !== undefined) {
			console.log(error.response);
			// TODO: pop up with error message
			return;
		}

		console.log('An unknown error has occurred');
		// TODO: pop up with error message
	};


	getDashboard = async () => {
		// TODO: can do these simultaneously
		await this.getSchedule(this.state.self.id);
		await this.getFriendSchedules();
	};


	createUser = async (email, username, password, remember) => {
		try {
			const createUserResponse = await youfree.createUser(email, username, password);

			const token = createUserResponse.headers.token;
			const user = createUserResponse.data;

			if (remember) {
				localStorage.setItem('token', token);
				localStorage.setItem('self', JSON.stringify(user));
			}

			this.setState({
				token: token,
				self: user
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
				localStorage.setItem('self', JSON.stringify(user));
			}

			this.setState({
				token: token,
				self: user
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
				self: null
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


	// TODO: Fetching someone else's schedule shouldn't affect self-schedule
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


	getSelfSchedule = () => {
		return this.getSchedule(this.state.self.id);
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


	deleteFriend = async (userId) => {
		try {
			await youfree.deleteFriend(this.state.token, userId);
		} catch (error) {
			this.handleError(error);
		}
	};


	render() {
		let content;
		if (this.state.token == null) {
			content = <AuthenticationPage onLoginSubmit={this.login}
			                              onCreateUserSubmit={this.createUser}/>;
		} else {
			content = (
				<Switch>
					<Route path="/search">
						<People token={this.state.token}
						        addFriend={this.addFriend}
						        deleteFriend={this.deleteFriend}
						        handleError={this.handleError}/>
					</Route>
					<Route path="/profile">
						<MyProfilePage user={this.state.self}
						               schedule={this.state.schedule}
						               getSchedule={this.getSelfSchedule}
						               addInterval={this.addInterval}/>
					</Route>
					<Route path="/about">
						<AboutPage/>
					</Route>
					<Route path="/">
						<Dashboard getDashboard={this.getDashboard}
						           schedule={this.state.schedule ? this.state.schedule[this.state.currentDay] : null}
						           friends={this.state.friendSchedules}
						           day={this.state.currentDay}
						           onAddInterval={this.addInterval}/>
					</Route>
				</Switch>
			);
		}

		return (
			<ThemeProvider theme={this.state.theme === 'light' ? lightTheme : darkTheme}>
				<Router>
					<Header isLoggedIn={!!this.state.token} theme={this.state.theme} logout={this.logout} invertTheme={this.invertTheme} />
					<div id="content">
						{content}
					</div>
				</Router>
			</ThemeProvider>
		);
	}
}

export default App;
