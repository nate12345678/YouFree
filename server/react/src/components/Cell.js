import React from 'react';

class Cell extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            interval: props.interval,
        };
    }

    componentDidMount() {
        this.setState({
            styles: {
                left: this.props.interval.startMin/1440*100 + "%",
                right: 100-this.props.interval.endMin/1440*100 + "%",
            }
        })
    }

    toTimes(interval) {
        let startPeriod = '';
        let endPeriod = '';
        let startHour = parseInt(interval.startMin/60);
        let endHour = parseInt(interval.endMin/60);
        let startMinute = parseInt(interval.startMin%60);
        let endMinute = parseInt(interval.endMin%60);
        if (startHour > 12) {
            startHour-=12;
            startPeriod='pm';
        } else {
            startPeriod='am';
        }
        if (endHour > 12) {
            endHour-=12;
            endPeriod='pm';
        } else {
            endPeriod='am';
        }
        return startHour + ':' + ('0' + startMinute).slice(-2) + startPeriod + '-' + endHour + ':' + ('0' + endMinute).slice(-2) + endPeriod;
    }

    render() {
        return (
            <a className="Cell" href='#' onClick={(event) => this.props.cellAction(this.state.interval.id, event)} style={this.state.styles}>
                {this.toTimes(this.props.interval)}
            </a>
        );
    }
}

export default Cell;