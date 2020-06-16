import '../css/Header.css';
import React from 'react';
import {
	AppBar,
	Icon,
	Toolbar,
	Typography
} from '@material-ui/core';

function Header(props) {

	function invertTheme() {
		const body = document.body;

		if (body.classList.replace('light', 'dark')) {
			return;
		}

		body.classList.replace('dark', 'light');
	}


	return (
		<AppBar id="appBar" position="sticky">
			<Toolbar>
				<Typography id="home" variant="h5" color="inherit">You Free</Typography>
				<Icon id="lightModeIcon" color="inherit" onClick={invertTheme}>cloud</Icon>
			</Toolbar>
		</AppBar>
	);
}

export default Header;
