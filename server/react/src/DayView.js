import React from 'react';

function DayView(props) {
    let content = [];

    for (let i = 0; i < props.intervals.length; i++) {
        if (i === 0) {
            content.push(
                <button className={"timeBlock"} onClick={() => alert('Details')} style={{
                    width: (props.intervals[i][1] - props.intervals[i][0]) / 24 * 100 + "%",
                    marginLeft: (props.intervals[i][0]) / 24 * 100 + "%"
                }}>
                    {props.intervals[i][0]} - {props.intervals[i][1]} hours
                </button>
            )
        } else {
            content.push(
                <button className={"timeBlock"} onClick={() => alert('Details')} style={{
                    width: (props.intervals[i][1] - props.intervals[i][0]) / 24 * 100 + "%",
                    marginLeft: (props.intervals[i][0] - props.intervals[i - 1][1]) / 24 * 100 + "%"
                }}>
                    {props.intervals[i][0]} - {props.intervals[i][1]} hours
                </button>
            )
        }
    }

    return (
        <div style={{
            display: 'flex',
            flexWrap: 'nowrap',
            flexDirection: 'row',
            alignItems: 'center',
            // borderTop: '2px solid deeppink'
        }}>
            <h1 style={{
                width: 75 + 'px',
                textAlign: 'center',
                borderRight: '2px solid deeppink',
                color: 'deeppink'
            }}>
                {props.day}
            </h1>
            <div style={{
                display: 'flex',
                flexWrap: 'nowrap',
                flexDirection: 'row',
                justifyContent: 'center',
                width: 100 + "%",
                margin: '0px 5px'
            }}>
                {content}
            </div>
        </div>
    )
}

export default DayView;