import React from 'react';
import MuiAlert from '@material-ui/lab/Alert';
import {
	Snackbar
} from '@material-ui/core';


const SNACKBAR_ORIGIN = {
	vertical: 'top',
	horizontal: 'center'
};

export default function ErrorSnackbar({ message, resetError }) {

	const handleClose = (event, reason) => {
		if (reason === 'clickaway') {
			return;
		}

		resetError();
	}

	return (
		<Snackbar anchorOrigin={SNACKBAR_ORIGIN} open={!!message} autoHideDuration={5000} onClose={handleClose}>
			<MuiAlert onClose={handleClose} severity="error">{message}</MuiAlert>
		</Snackbar>
	);
}
