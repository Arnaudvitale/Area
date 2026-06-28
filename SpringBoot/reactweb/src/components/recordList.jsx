import React, { useEffect, useState } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faKey, faCheck, faTimes, faTrash } from '@fortawesome/free-solid-svg-icons';
import { faDeezer, faGithub } from '@fortawesome/free-brands-svg-icons';
import '../css/recordList.css';

const Automation = (props) => (
    <tr className={props.automation.activated ? 'automation-activated' : 'automation-deactivated'}>
        <td>{props.automation.label}</td>
        <td>{props.automation.description}</td>
        <td>{props.automation.activated ? 'Activated' : 'Deactivated'}</td>
        <td>
            <button
                className="btn btn-delete"
                onClick={() => props.deleteAutomation(props.automation._id)}>
                <FontAwesomeIcon icon={faTrash} />
            </button>
        </td>
    </tr>
);

export default function RecordList() {
    const [records, setRecords] = useState([]);
    const [isUserAuthenticated, setIsUserAuthenticated] = useState(false);
    const [activateAutomations, setActivateAutomations] = useState(false);
    const [user, setUser] = useState(null);
    const [timer, setTimer] = useState(5);

    useEffect(() => {

        setUser(JSON.parse(localStorage.getItem('user')));

        async function checkAuthentication() {
            try {
                const response = await fetch(`http://localhost:8080/auth/check`);
                const data = await response.json();

                if (data.status === "success") {
                setIsUserAuthenticated(true);
                } else {
                setIsUserAuthenticated(false);
                }
            } catch (error) {
                console.error("Erreur lors de la vérification de l'authentification", error);
            }
        }

        checkAuthentication();

        const intervalId = setInterval(() => {
            if (isUserAuthenticated && records.length > 0 && activateAutomations) {
                callActions(user.email);
            }
        }, timer * 1000);

        return () => clearInterval(intervalId);
    }, [isUserAuthenticated, records, activateAutomations]);

    useEffect(() => {

        async function getRecords() {
            try {

                var userInfos = JSON.parse(localStorage.getItem('user'));

                const response = await fetch(`http://localhost:8080/automations/${userInfos.email}`);
                if (!response.ok) {
                    throw new Error(`An error occurred: ${response.statusText}`);
                }
                const records = await response.json();
                setRecords(records);
            } catch (error) {
                console.error("Erreur lors de la récupération des enregistrements", error);
            }
        }

        getRecords();
    }, []);

    function getOAuthToken() {
        fetch(`http://localhost:8080/auth`)
        .then((response) => response.json())
        .then((data) => {
            window.location.href = data.url;
            setIsUserAuthenticated(true);
        })
        .catch((error) => {
            console.error("Erreur lors de l'obtention du jeton OAuth", error);
            window.alert("Erreur lors de l'obtention du jeton OAuth");
        });
    }

    function getGitHubToken() {
        window.location.href = `http://localhost:8080/auth/github?userEmail=${user.email}`;
    }

    function getDeezerToken() {
        fetch(`http://localhost:8080/auth/deezer?userEmail=${user.email}`)
        .then((response) => response.json())
        .then((data) => {
            window.location.href = data.url;
        })
        .catch((error) => {
            console.error("Erreur lors de l'obtention du jeton OAuth", error);
            window.alert("Erreur lors de l'obtention du jeton OAuth");
        });
    }

    function callActions(email) {

        console.log("Calling APIs");

        for (const record of records) {
            if (record.activated === false)
                continue;
            for (const action of record.actions) {

                const service = action.split('-')[0];
                const actionName = action.split('-')[1].split('/')[0];
                const params = action.split('/')[1];

                fetch(`http://localhost:8080/api/${service}/${actionName}?userEmail=${email}&${params}`)
                .then((response) => response.json())
                .then((data) => {
                    console.log("Data message: " + data.message);
                    console.log("Data code: " + data.code);
                    if (data.code === 1) {
                        callReactions(record, data.message, email);
                    }
                })
                .catch((error) => {
                    console.error("Erreur lors de l'appel des APIs", error);
                });
            }
        }
    }

    function callReactions(record, message) {
        if (record === undefined)
            return;
        for (const reaction of record.reactions) {
            const service = reaction.split('-')[0];
            const reactionName = reaction.split('-')[1].split('/')[0];
            const params = reaction.split('/')[1];


            fetch(`http://localhost:8080/api/${service}/${reactionName}?message=${message}&userEmail=${user.email}&${params}`)
            .then((response) => response.json())
            .then((data) => {
                console.log("Data message: " + data.message);
                console.log("Data code: " + data.code);
            })
            .catch((error) => {
                console.error("Erreur lors de l'appel des APIs", error);
            });
        }
    }

    function deleteAutomation(id) {
        fetch(`http://localhost:8080/automation/${id}`, {
        method: "DELETE",
        })
        .then((response) => response.json())
        .then((data) => {
            console.log(data.message);
            setRecords((prevRecords) => prevRecords.filter((record) => record._id !== id));
        })
        .catch((error) => {
            console.error("Erreur lors de la suppression de l'automatisation", error);
            window.alert("Erreur lors de la suppression de l'automatisation");
        });
    }

    function automationsList() {

        return records.map((record) => (
            <Automation
                automation={record}
                deleteAutomation={deleteAutomation}
                key={record._id}
            />
        ));
    }

    return (
        <div>
            <h3>Automations</h3>
            <table className="table">
                <thead className="thead-light">
                <tr>
                    <th>Label</th>
                    <th>Descriptions</th>
                    <th>Automations</th>
                </tr>
                </thead>
                <tbody>{automationsList()}</tbody>
            </table>
            <br />
            <p style={{color: 'red', textAlign: 'center'}}>Veuillez vous connecter pour accéder aux fonctionnalités de l'application.</p>
            <div className="buttons-container">
                <button className="btn btn-link" onClick={getOAuthToken} disabled={isUserAuthenticated}>
                    <FontAwesomeIcon icon={faKey} /> oauth2 connection
                </button>
                <button className="btn btn-link" onClick={getGitHubToken}>
                    <FontAwesomeIcon icon={faGithub} /> github connection
                </button>
                <button className="btn btn-link" onClick={getDeezerToken}>
                    <FontAwesomeIcon icon={faDeezer} /> Deezer connection
                </button>
                <button
                    className="btn btn-link"
                    onClick={() => setActivateAutomations(!activateAutomations)}
                    disabled={!isUserAuthenticated}>
                    <FontAwesomeIcon icon={activateAutomations ? faCheck : faTimes} /> Automations {activateAutomations ? ' Activated' : ' Deactivated'}
                </button>
                <label id="timer">
                    Timer (in seconds): <input id="time" type="number" value={timer} max="10" min="2" onChange={(e) => setTimer(e.target.value)} />
                </label>
            </div>
        </div>
    );
}
