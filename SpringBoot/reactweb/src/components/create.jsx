import React, { useState } from "react";
import { useNavigate } from "react-router";
import '../css/create.css';

const Actions = {
    "Calendar-checkNewEvent": "checkNewEvent",
    "Drive-checkNewFiles": "checkNewFiles",
    "Drive-checkFileDescription": "checkFileDescription",
    "Gmail-checkNewEmails": "checkNewEmails",
    "GitHub-checkNewCommits": "checkNewCommits",
    "GitHub-checkNewPullRequests": "checkNewPullRequests",
    "GitHub-checkNewIssues": "checkNewIssues",
    "Youtube-checkNewSubcribers": "checkNewSubcribers",
    "Youtube-checkLostSubcribers": "checkLostSubcribers",
    "Youtube-checkMostPopularVideo": "checkMostPopularVideo",
    "Deezer-checkNewMusicInPlaylist": "checkNewMusicInPlaylist",
    "Deezer-checkRemovedMusicInPlaylist": "checkRemovedMusicInPlaylist",
}

const Reactions = {
    "Gmail-sendEmail": "sendEmail",
    "GitHub-postAutomatedComment": "postAutomatedComment",
    "Deezer-addMusicToPlaylist": "addMusicToPlaylist",
}

export default function Create() {
    const [form, setForm] = useState({
        label: "",
        description: "",
        actions: [],
        reactions: [],
        activated: false,
    });

    const [parameters, setParameters] = useState({
        actionParameters: "",
        reactionParameters: "",
    });

    function updateParameters(value) {
        return setParameters((prev) => {
            return { ...prev, ...value };
        });
    }

    const user = JSON.parse(localStorage.getItem('user'));

    const navigate = useNavigate();

    function updateForm(value) {
        return setForm((prev) => {
            return { ...prev, ...value };
        });
    }

    async function onSubmit(e) {
        e.preventDefault();
        const newAutomation = { ...form };
        newAutomation.actions = form.actions + "/" + parameters.actionParameters;
        newAutomation.reactions = form.reactions + "/" + parameters.reactionParameters;
        await fetch(`http://localhost:8080/automations/add?userEmail=${user.email}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newAutomation),
        })
        .catch(error => {
            window.alert(error);
            return;
        });
        setForm({ label: "", description: "", actions: [], reactions: [], activated: false });
        navigate("/");
    }

    const reactionOptions = Object.keys(Reactions).map((key, index) => {
        return <option value={key} key={index}>{Reactions[key]}</option>;
    });

    const actionOptions = Object.keys(Actions).map((key, index) => {
        return <option value={key} key={index}>{Actions[key]}</option>;
    });

    return (
        <div>
            <h3>Create New Automation</h3>
            <form onSubmit={onSubmit}>
                <div className="name-aut">
                    <label>Label: </label>
                    <input
                        type="text"
                        required
                        className="name-input"
                        value={form.label}
                        onChange={(e) => updateForm({ label: e.target.value })}
                    />
                </div>
                <div className="des-aut">
                    <label>Description: </label>
                    <input
                        type="text"
                        required
                        className="description-input"
                        value={form.description}
                        onChange={(e) => updateForm({ description: e.target.value })}
                    />
                </div>

                <div className="action-aut">
                    <label>Actions: </label>
                    <select className="select-aut" value={form.actions} onChange={(e) => updateForm({ actions: e.target.value })}>
                        <option value=""></option>
                        {actionOptions}
                    </select>
                </div>
                <div className="form-group">
                    <label>Action parameters</label>
                    <input
                        type="text"
                        className="form-control"
                        value={form.actionParameters}
                        onChange={(e) => updateParameters({ actionParameters: e.target.value })}
                    />
                </div>
                <div className="reac-aut">
                    <label>Reactions: </label>
                    <select className="select-aut" value={form.reactions} onChange={(e) => updateForm({ reactions: e.target.value })}>
                        <option value=""></option>
                        {reactionOptions}
                    </select>
                </div>
                <div className="form-group">
                    <label>Reaction parameters</label>
                    <input
                        type="text"
                        className="form-control"
                        value={form.reactionParameters}
                        onChange={(e) => updateParameters({ reactionParameters: e.target.value })}
                    />
                </div>
                <div className="question-aut">
                    <label>Activate ?</label>
                    <input
                        type="checkbox"
                        checked={form.activated}
                        onChange={(e) => updateForm({ activated: e.target.checked })}
                        style={{ width: "20px", height: "20px" }}
                    />
                </div>
                    <input
                        type="submit"
                        value="Create Automation"
                        className="submit-btn"
                    />
            </form>
        </div>
    );
}