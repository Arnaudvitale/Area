import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router";


const Actions = {
    "Calendar-checkCalendar": "checkCalendar",
    "Calendar-checkEventDescription": "checkEventDescription",
    "Drive-checkNewFiles": "checkNewFiles",
    "Drive-checkFileDescription": "checkFileDescription",
    "Gmail-checkNewEmails": "checkNewEmails",
    "GitHub-checkNewCommits": "checkNewCommits",
    "GitHub-checkNewPullRequests": "checkNewPullRequests",
    "GitHub-checkNewIssues": "checkNewIssues",
    "Youtube-checkNewSubcribers": "checkNewSubcribers",
    "Youtube-checkLostSubcribers": "checkLostSubcribers",
    "Youtube-chekMostPopularVideo": "checkMostPopularVideo",
    "Deezer-checkNewMusicInPlaylist": "checkNewMusicInPlaylist",
    "Deezer-checkRemovedMusicInPlaylist": "checkRemovedMusicInPlaylist",
}

const Reactions = {
    "Gmail-sendEmail": "sendEmail",
    "GitHub-postAutomatedComment": "postAutomatedComment",
}

export default function Edit() {
    const [form, setForm] = useState({
        label: "",
        description: "",
        actions: [],
        reactions: [],
        activated: false,
    });

    const params = useParams();
    const navigate = useNavigate();
    useEffect(() => {
        async function fetchData() {
            const id = params.id.toString();
            const response = await fetch(`http://localhost:8080/automation/${params.id.toString()}`);
            if (!response.ok) {
                const message = `An error has occurred: ${response.statusText}`;
                window.alert(message);
                return;
            }
            const record = await response.json();
            if (!record) {
                window.alert(`Record with id ${id} not found`);
                navigate("/");
                return;
            }
            setForm(record);
        }
        fetchData();
        return;
    }, [params.id, navigate]);

    function updateForm(value) {
        return setForm((prev) => {
            return { ...prev, ...value };
        });
    }

    async function onSubmit(e) {
        e.preventDefault();
        const editedPerson = {
            label: form.label,
            description: form.description
        };
        await fetch(`http://localhost:8080/automation/update/${params.id}`, {
            method: "POST",
            body: JSON.stringify(editedPerson),
            headers: {
            'Content-Type': 'application/json'
            },
        });
        navigate("/");
    }

    const reactionOptions = Object.keys(Reactions).map((key) => {
        return <option value={key}>{Reactions[key]}</option>;
    });

    const actionOptions = Object.keys(Actions).map((key) => {
        return <option value={key}>{Actions[key]}</option>;
    });

    return (
        <div>
            <h3>Update Record</h3>
            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Label: </label>
                    <input
                        type="text"
                        className="form-control"
                        value={form.label}
                        onChange={(e) => updateForm({ label: e.target.value })}
                    />
                </div>
                <div className="form-group">
                    <label>Description: </label>
                    <input
                        type="text"
                        className="form-control"
                        value={form.description}
                        onChange={(e) => updateForm({ description: e.target.value })}
                    />
                </div>

                <div className="form-group">
                    <label>Actions: </label>
                    <select className="form-control" value={form.actions} onChange={(e) => updateForm({ actions: e.target.value })}>
                        <option value=""></option>
                        {actionOptions}
                    </select>
                </div>
                <div className="form-group">
                    <label>Reactions: </label>
                    <select className="form-control" value={form.reactions} onChange={(e) => updateForm({ reactions: e.target.value })}>
                        <option value=""></option>
                        {reactionOptions}
                    </select>
                </div>
                <div className="form-group">
                    <label>Activate ?</label>
                    <input
                        type="checkbox"
                        checked={form.activated}
                        onChange={(e) => updateForm({ activated: e.target.checked })}
                        style={{ width: "20px", height: "20px" }}
                    />
                </div>

                <div className="form-group">
                    <input type="submit" value="Update Record" className="btn btn-primary" />
                </div>
            </form>
        </div>
    );
}