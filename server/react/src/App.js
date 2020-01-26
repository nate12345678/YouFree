import React from 'react';
import Login from "./Login";

class App extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            login: <Login/>,
            loggedIn: false //true if successfully logged in
        }
    }

    render() {
        let content = <Login/>
        return(
            <div>
                {content}
            </div>
        );
    }
}

export default App;
