import '../css/Header.css';
import React from 'react';
import { NavLink } from 'react-router-dom';
import {
	AppBar,
	Badge,
	Button,
	Drawer,
	Icon,
	IconButton,
	Toolbar,
	Tooltip,
	Typography,
	useMediaQuery
} from '@material-ui/core';
import {
	logout,
	setTheme
} from '../state/Effects';
import { connect } from 'react-redux';


function select(state) {
	return {
		isLoggedIn: !!state.token,
		theme: state.theme,
		numNotifications: state.notifications.count
	};
}


function mapDispatchToProps(dispatch) {
	return {
		logout: () => dispatch(logout()),
		setTheme: (theme) => dispatch(setTheme(theme))
	};
}

const title = <Typography variant="h5" color="inherit">You Free</Typography>;

function ConnectedHeader(props) {

	const routes = [
		{
			route: '/',
			label: 'Home',
			badge: null,
			icon: 'house'
		},
		{
			route: '/friends',
			label: 'Friends',
			badge: props.numNotifications,
			icon: 'people'
		},
		{
			route: '/profile',
			label: 'My Profile',
			badge: null,
			icon: 'account_circle'
		},
		// {
		// 	route: '/about',
		// 	label: 'About',
		// 	badge: null
		// }
	];

	const invertTheme = () => {
		props.setTheme(props.theme === 'light' ? 'dark' : 'light');
	}

	const [open, setOpen] = React.useState();
	const toggleDrawer = (isOpen) => () => setOpen(isOpen);
	const isDesktop = useMediaQuery('(min-width: 720px)');
	const themeButton = <ThemeButton hasLeftMargin={!(isDesktop && props.isLoggedIn)} theme={props.theme} invertTheme={invertTheme} />;

	let toolbar;
	let drawer;
	if (props.isLoggedIn) {
		toolbar = isDesktop
			? <DesktopToolbar routes={routes} onLogout={props.logout} themeButton={themeButton} />
			: <MobileToolbar onMenuClick={toggleDrawer(true)} themeButton={themeButton} />;

		drawer = isDesktop
			? null
			: <MyDrawer routes={routes} isOpen={open} onClose={toggleDrawer(false)} onLogout={props.logout} />;
	} else {
		toolbar = <EmptyToolbar themeButton={themeButton} />;
		drawer = null;
	}

	return (
		<React.Fragment>
			<AppBar id="appBar" position="sticky" color={props.theme === 'light' ? 'primary' : 'default'}>
				<Toolbar>
					{toolbar}
				</Toolbar>
			</AppBar>
			{drawer}
		</React.Fragment>
	);
}


function MyDrawer({ routes, isOpen, onClose, onLogout }) {
	const links = routes.map(route => {
		let link = <span className="drawer-nav-link-label">{route.label}</span>;

		if (!!route.badge) {
			link = <Badge color="primary" badgeContent={route.badge}>{link}</Badge>
		}

		return (
			<NavLink key={route.route} exact to={route.route} className="drawer-nav-link" activeClassName="drawer-nav-link-active" onClick={onClose}>
					<Icon className="drawer-nav-link-icon">{route.icon}</Icon>
					{link}
			</NavLink>
		);
	});

	// TODO: Make the logout button a proper button for accessibility
	return (
		<Drawer className="drawer" anchor="left" open={isOpen} onClose={onClose}>
			<div className="drawer-content">
				<div className="drawer-header">
					{title}
					<div>Beta Test</div>
				</div>
				<div className="drawer-divider" />
				<ul className="drawer-nav">
					{links}
				</ul>
			</div>
			<div className="drawer-divider"/>
			<div className="drawer-logout" onClick={onLogout}>
				<Icon className="drawer-nav-link-icon">logout</Icon>
				Logout
			</div>
		</Drawer>
	);
}


function ThemeButton({ hasLeftMargin, theme, invertTheme }) {
	return (
		<Tooltip title="Toggle light/dark theme">
			<IconButton className={hasLeftMargin ? 'theme-button-mobile' : ''} color="inherit" onClick={invertTheme}>
				<Icon color="inherit">{theme === 'light' ? 'dark_mode' : 'light_mode'}</Icon>
			</IconButton>
		</Tooltip>
	);
}


function EmptyToolbar({ themeButton }) {
	return (
		<React.Fragment>
			{title}
			{themeButton}
		</React.Fragment>
	)
}


function DesktopToolbar({ routes, onLogout, themeButton }) {
	const links = routes.map(route => {
		let label = <span className="header-nav-label">{route.label}</span>;

		if (!!route.badge) {
			label = <Badge color="secondary" badgeContent={route.badge}>{label}</Badge>;
		}

		return (
			<li key={route.route} className="header-nav-item">
				<NavLink exact to={route.route} className="header-nav-link" activeClassName="header-nav-link-active">
					{label}
				</NavLink>
			</li>
		);
	});

	return (
		<React.Fragment>
			{title}
			<ul className="header-nav-list">
				{links}
			</ul>
			<Button id="logout-button" variant="text" color="inherit" onClick={onLogout} disableElevation>Logout</Button>
			{themeButton}
		</React.Fragment>
	);
}


function MobileToolbar({ onMenuClick, themeButton }) {
	return (
		<React.Fragment>
			<IconButton className="menu-button" onClick={onMenuClick}>
				<Icon color="inherit">menu</Icon>
			</IconButton>
			<NavLink className="title-link" exact to="/">
				{title}
			</NavLink>
			{themeButton}
		</React.Fragment>
	);
}

const Header = connect(select, mapDispatchToProps)(ConnectedHeader);
export default Header;
