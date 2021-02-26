import '../css//App.css';
import React from 'react';
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
						<MyProfilePage/>
					</Route>
					{/*<Route path="/about">*/}
					{/*	<AboutPage/>*/}
					{/*</Route>*/}
					<Route path="/">
						<Dashboard/>
					</Route>
				</Switch>
			);
		}

		return (
			<ThemeProvider theme={this.props.theme === 'light' ? lightTheme : darkTheme}>
				<Router>
					<Header/>
					<div className="app-content">
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
