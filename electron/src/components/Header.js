import '../css/Header.css';
import React from 'react';
import {
	NavLink
} from 'react-router-dom';
import {
	AppBar,
	Button,
	Drawer,
	Icon,
	IconButton,
	Toolbar,
	Tooltip,
	Typography,
	useMediaQuery
} from '@material-ui/core';


const routes = [
	{ route: '/', label: 'Home' },
	{ route: '/search', label: 'Search' },
	{ route: '/profile', label: 'My Profile' },
	{ route: '/about', label: 'About' }
];

const title = <Typography id="home" variant="h5" color="inherit">Voice Tracker</Typography>;

export default function Header(props) {

	const [open, setOpen] = React.useState();
	const toggleDrawer = (isOpen) => () => setOpen(isOpen);
	const isDesktop = useMediaQuery('(min-width: 720px)');
	const themeButton = <ThemeButton hasLeftMargin={!(isDesktop && props.isLoggedIn)} theme={props.theme} invertTheme={props.invertTheme} />;

	let toolbar;
	let drawer;
	if (props.isLoggedIn) {
		toolbar = isDesktop
			? <DesktopToolbar onLogout={props.logout} themeButton={themeButton} />
			: <MobileToolbar onMenuClick={toggleDrawer(true)} themeButton={themeButton} />;

		drawer = isDesktop
			? null
			: <MyDrawer isOpen={open} onClose={toggleDrawer(false)} />;
	} else {
		toolbar = <EmptyToolbar themeButton={themeButton} />;
		drawer = null;
	}

	return (
		<React.Fragment>
			<AppBar id="appBar" position="sticky">
				<Toolbar>
					{toolbar}
				</Toolbar>
			</AppBar>
			{drawer}
		</React.Fragment>
	);
}


function MyDrawer({ isOpen, onClose }) {
	return (
		<Drawer className="drawer" anchor="left" open={isOpen} onClose={onClose}>
			<div className="drawer-content">
				<div className="drawer-header">
					{title}
					<div>Beta Test</div>
				</div>
				<div className="drawer-divider" />
				<ul className="drawer-nav">
					{
						routes.map(route => (
							<NavLink key={route.route} exact to={route.route} className="drawer-nav-link" activeClassName="drawer-nav-link-active" onClick={onClose}>
								<div className="drawer-nav-link-label">{route.label}</div>
							</NavLink>
						))
					}
				</ul>
			</div>
		</Drawer>
	);
}


function ThemeButton({ hasLeftMargin, theme, invertTheme }) {
	return (
		<Tooltip title="Toggle light/dark theme">
			<IconButton className={hasLeftMargin ? 'theme-button-mobile' : ''} color="inherit" onClick={invertTheme}>
				<Icon color="inherit">{theme === 'light' ? 'brightness_2' : 'brightness_5'}</Icon>
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


function DesktopToolbar({ onLogout, themeButton }) {
	return (
		<React.Fragment>
			{title}
			<ul className="header-nav-list">
				{
					routes.map(route => (
						<li key={route.route} className="header-nav-item">
							<NavLink exact to={route.route} className="header-nav-link" activeClassName="header-nav-link-active">
								<span>{route.label.toUpperCase()}</span>
							</NavLink>
						</li>
					))
				}
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
			{title}
			{themeButton}
		</React.Fragment>
	);
}
