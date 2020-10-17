import '../css//App.css';
import React from 'react';
import youfree from '../api/Youfree';
import Dashboard from './Dashboard';
import Header from './Header';
import AuthenticationPage from './login/AuthenticationPage';
import PeoplePage from './people/PeoplePage';
import {
	BrowserRouter as Router,
	Route,
	Switch
} from 'react-router-dom';
import { ThemeProvider } from '@material-ui/core/styles';
// import AboutPage from './AboutPage';
import MyProfilePage from './MyProfilePage';
import ErrorSnackbar from './common/ErrorSnackbar';
import { connect } from 'react-redux';
import {
	clearError,
	setError,
	setSelf,
	setTheme,
	setToken
} from '../state/Actions';
import {
	darkTheme,
	lightTheme
} from '../models/Themes';
import {
	createUser,
	login,
	logout
} from '../state/Effects';


function select(state) {
	return {
		token: state.token,
		self: state.self,
		theme: state.theme,
		errorMessage: state.errorMessage
	}
}


function mapDispatchToProps(dispatch) {
	return {
		setError: (message) => dispatch(setError(message)),
		setSelf: (self) => dispatch(setSelf(self)),
		setTheme: (theme) => dispatch(setTheme(theme)),
		setToken: (token) => dispatch(setToken(token)),
		clearError: () => dispatch(clearError()),
		createUser: (email, username, password, remember) => dispatch(createUser(email, username, password, remember)),
		login: (email, password, remember) => dispatch(login(email, password, remember)),
		logout: () => dispatch(logout())
	};
}


const initialState = {
	schedule: null,
	friendSchedules: null,
};


class ConnectedApp extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			...initialState,
		};
	}


	componentDidMount() {
		// Auto-login
		const token = localStorage.getItem('token');
		const self = JSON.parse(localStorage.getItem('self'));
		if (token && self) {
			this.props.setToken(token);
			this.props.setSelf(self);
		}

		// Load theme
		const theme = localStorage.getItem('theme');
		if (theme) {
			this.props.setTheme(theme);
		}
	}


	invertTheme = () => {
		const from = this.props.theme;
		const to = this.props.theme === 'light' ? 'dark' : 'light';

		this.props.setTheme(to);
		document.body.classList.replace(from, to);
		localStorage.setItem('theme', to);
	}


	handleError = (error) => {
		if (error.response !== undefined) {
			console.log(error.response);
			this.props.setError(error.response.data);
			return;
		}

		console.log('An unknown error has occurred');
		this.props.setError('An unknown error has occurred');
	};


	getDashboard = async () => {
		// TODO: can do these simultaneously
		await this.getSchedule(this.props.self.id);
		await this.getFriendSchedules();
	};


	addInterval = async (dayOfWeek, startMin, endMin) => {
		try {
			const addIntervalReq = await youfree.addInterval(this.props.token, dayOfWeek, startMin, endMin);

			console.log('Added interval');
			const schedule = addIntervalReq.data;

			this.setState({
				schedule: schedule
			});
		} catch (error) {
			this.handleError(error);
		}
	};


	updateInterval = async (intervalId, dayOfWeek, startMin, endMin) => {
		try {
			const updateIntervalResponse = await youfree.updateInterval(this.props.token, intervalId, dayOfWeek, startMin, endMin);

			console.log('Updated interval');
			const schedule = updateIntervalResponse.data;

			this.setState({
				schedule: schedule
			});
		} catch (error) {
			this.handleError(error);
		}
	};


	deleteInterval = async (intervalId) => {
		// TODO: save old schedule and make copy of new one
		try {
			// TODO: locally remove interval from copy of schedule, set state to new schedule
			const delIntervalResponse = await youfree.deleteInterval(this.props.token, intervalId);

			console.log('Deleted interval');
			const schedule = delIntervalResponse.data;

			this.setState({
				schedule: schedule
			});
		} catch (error) {
			this.handleError(error);
			// TODO: revert to old schedule
		}
	};


	// TODO: Fetching someone else's schedule shouldn't affect self-schedule
	getSchedule = async (userId) => {
		let schedule = null;
		try {
			const getScheduleResponse = await youfree.getSchedule(this.props.token, userId);
			schedule = getScheduleResponse.data;

			this.setState({
				schedule: schedule
			});

		} catch (error) {
			this.handleError(error);
		}
	};


	getSelfSchedule = () => {
		return this.getSchedule(this.props.self.id);
	};


	getFriendSchedules = async () => {
		try {
			const getFriendSchedulesResponse = await youfree.getFriendSchedules(this.props.token);

			this.setState({
				friendSchedules: getFriendSchedulesResponse.data
			});
		} catch (error) {
			this.handleError(error);
		}
	};


	addFriend = async (userId) => {
		try {
			await youfree.addFriend(this.props.token, userId);

		} catch (error) {
			this.handleError(error);
		}
	};


	deleteFriend = async (userId) => {
		try {
			await youfree.deleteFriend(this.props.token, userId);
		} catch (error) {
			this.handleError(error);
		}
	};


	render() {
		let content;
		if (this.props.token == null || this.props.self == null) {
			content = <AuthenticationPage onLoginSubmit={this.props.login}
			                              onCreateUserSubmit={this.props.createUser}
			                              handleError={this.handleError}/>;
		} else {
			content = (
				<Switch>
					<Route path="/friends">
						<PeoplePage token={this.props.token}
						            addFriend={this.addFriend}
						            deleteFriend={this.deleteFriend}
						            handleError={this.handleError}/>
					</Route>
					<Route path="/profile">
						<MyProfilePage onAddInterval={this.addInterval}
						               onUpdateInterval={this.updateInterval}
						               onDeleteInterval={this.deleteInterval}
						/>
					</Route>
					{/*<Route path="/about">*/}
					{/*	<AboutPage/>*/}
					{/*</Route>*/}
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
			<ThemeProvider theme={this.props.theme === 'light' ? lightTheme : darkTheme}>
				<Router>
					<Header invertTheme={this.invertTheme} />
					<div id="content">
						{content}
					</div>
				</Router>
				<ErrorSnackbar />
			</ThemeProvider>
		);
	}
}

const App = connect(select, mapDispatchToProps)(ConnectedApp);
export default App;
