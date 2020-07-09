import '../css/Header.css';
import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import {
	AppBar,
	Button,
	Icon,
	IconButton,
	Toolbar,
	Tooltip,
	Typography
} from '@material-ui/core';

function Header(props) {

	const history = useHistory();
	const [theme, setTheme] = useState('light');

	function invertTheme() {
		const body = document.body;

		if (theme === 'light') {
			setTheme('dark');
			body.classList.replace('light', 'dark')
			return;
		}

		setTheme('light');
		body.classList.replace('dark', 'light');
	}


	return (
		<AppBar id="appBar" position="sticky">
			<Toolbar>
				<Typography id="home" variant="h5" color="inherit">You Free</Typography>
				<Button id="tab-home" variant="contained" color="secondary" onClick={() => history.push('/')} disableElevation>Home</Button>
				<Button id="tab-search" variant="contained" color="secondary" onClick={() => history.push('/search')} disableElevation>Friends</Button>
				<Button id="logout-button" variant="text" color="inherit" onClick={props.logout} disableElevation>Logout</Button>
				<Tooltip title="Toggle light/dark theme">
					<IconButton id="lightModeIcon" color="inherit" onClick={invertTheme}>
						<Icon>{theme === 'light' ? 'brightness_2' : 'brightness_5'}</Icon>
					</IconButton>
				</Tooltip>
			</Toolbar>
		</AppBar>
	);
}

export default Header;
