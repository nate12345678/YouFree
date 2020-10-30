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
} from '../state/Actions';
import {
	darkTheme,
	lightTheme
} from '../models/Themes';
import {
	initApp,
	logout,
	setTheme
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
		initApp: () => dispatch(initApp()),
		setError: (message) => dispatch(setError(message)),
		setTheme: (theme) => dispatch(setTheme(theme)),
		clearError: () => dispatch(clearError()),
		logout: () => dispatch(logout()),
	};
}


const initialState = {
	schedule: null
};


class ConnectedApp extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			...initialState,
		};
	}


	componentDidMount() {
		this.props.initApp();
	}


	invertTheme = () => {
		this.props.setTheme(this.props.theme === 'light' ? 'dark' : 'light');
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


	render() {
		let content;
		if (this.props.token == null || this.props.self == null) {
			content = <AuthenticationPage/>;
		} else {
			content = (
				<Switch>
					<Route path="/friends">
						<PeoplePage/>
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
						<Dashboard onAddInterval={this.addInterval}/>
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
