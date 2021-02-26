import { createMuiTheme } from '@material-ui/core';
import * as Colors from '@material-ui/core/colors';

export const lightTheme = createMuiTheme({
	palette: {
		primary: {
			main: Colors.indigo['500'],
			dark: Colors.indigo['700']
		},
		secondary: {
			main: Colors.pink['300']
		},
		red: {
			main: Colors.red['400']
		}
	}
});

export const darkTheme = createMuiTheme({
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
