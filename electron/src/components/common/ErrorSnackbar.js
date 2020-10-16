import React from 'react';
import MuiAlert from '@material-ui/lab/Alert';
import {
	Snackbar
} from '@material-ui/core';
import { connect } from 'react-redux';
import {
	clearError,
	setError
} from '../../state/Actions';


const SNACKBAR_ORIGIN = {
	vertical: 'top',
	horizontal: 'center'
};

function select(state) {
	return {
		message: state.errorMessage
	};
}

function mapDispatchToProps(dispatch) {
	return {
		setError: (message) => dispatch(setError(message)),
		clearError: () => dispatch(clearError())
	};
}

function ConnectedErrorSnackbar({ message, clearError }) {

	const handleClose = (event, reason) => {
		if (reason === 'clickaway') {
			return;
		}

		clearError();
	}

	return (
		<Snackbar anchorOrigin={SNACKBAR_ORIGIN} open={!!message} autoHideDuration={5000} onClose={handleClose}>
			<MuiAlert onClose={handleClose} severity="error">{message}</MuiAlert>
		</Snackbar>
	);
}

const ErrorSnackbar = connect(select, mapDispatchToProps)(ConnectedErrorSnackbar);
export default ErrorSnackbar;
