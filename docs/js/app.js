'use strict';

let currentRound = 1;
let totalScore = 0;

// Register level selection listeners for all 3 rounds
document.getElementById("levelSelection1").addEventListener("submit", levelSelect);
document.getElementById("levelSelection2").addEventListener("submit", levelSelect);
document.getElementById("levelSelection3").addEventListener("submit", levelSelect);

// Register guess listeners for all 3 rounds
document.getElementById("guessSelection1").addEventListener("submit", guessEntry);
document.getElementById("guessSelection2").addEventListener("submit", guessEntry);
document.getElementById("guessSelection3").addEventListener("submit", guessEntry);

document.getElementById("newGameBtn").classList.add("hidden");

function levelSelect(event) {
    event.preventDefault();
    const level = document.getElementById("levels" + currentRound).value;
    fetchLevel(level);
}

async function fetchLevel(level) {
    const response = await fetch('https://spfl-happy-hunting-grounds.hostless.app/level?userSelectedLevel=' + level);
    const levelData = await response.json();

    localStorage.setItem("levelData" + currentRound, JSON.stringify(levelData));

    document.getElementById("levelSelectSec" + currentRound).classList.add('hidden');
    document.getElementById("guessSec" + currentRound).classList.remove('hidden');
    document.getElementById("level" + currentRound).innerHTML = "Level " + level + " - " + levelData.team + " at " + levelData.ground;
}

function guessEntry(event) {
    event.preventDefault();
    const playerSelection = document.getElementById("player" + currentRound).value;
    const seasonSelection = document.getElementById("season" + currentRound).value;
    fetchGuess(playerSelection, seasonSelection);
}

async function fetchGuess(playerSelection, seasonSelection) {
    const text = localStorage.getItem("levelData" + currentRound);
    const obj = JSON.parse(text);
    const response = await fetch('https://spfl-happy-hunting-grounds.hostless.app//guess?team=' + obj.team + '&ground=' + obj.ground + '&season=' + seasonSelection + '&playerName=' + playerSelection + '&level=' + obj.level);
    const data = await response.json();

    if (!data.correct) {
        document.getElementById("result" + currentRound).innerHTML =
            "Incorrect. " + playerSelection + " did not score for " + obj.team + " at " + obj.ground + " in " + seasonSelection + ".";
    } else {
        totalScore += data.points;
        document.getElementById("score" + currentRound).innerHTML = "Score: " + data.points;
        document.getElementById("result" + currentRound).innerHTML =
            "Correct! +" + data.points + " points. " +
            data.homeTeam + " " + data.finalScore + " " + data.awayTeam +
            " (" + data.date + ")";
    }
    document.getElementById("totalScoreDisplay").innerHTML =
            "Total score: " + totalScore;

    // Advance to next round or show final score
    if (currentRound < 3) {
        document.getElementById("answerSubmit" + currentRound).disabled = true;
        currentRound++;
        document.getElementById("levels" + currentRound).disabled = false;
        document.getElementById("playBtn" + currentRound).disabled = false;
    } else {
        document.getElementById("newGameBtn").classList.remove("hidden");
    }
}

let debounceTimer = null;

function playerEntry(event) {
    event.preventDefault();
    const input = event.target;
    const query = input.value.trim();
    const roundNum = input.id.replace('player', '');
    const suggestionsList = document.getElementById('suggestions' + roundNum);

    clearTimeout(debounceTimer);

    if (query.length < 2) {
        suggestionsList.innerHTML = '';
        return;
    }

    debounceTimer = setTimeout(() => fetchPlayer(query, roundNum), 300);
}

async function fetchPlayer(query, roundNum) {
    const suggestionsList = document.getElementById('suggestions' + roundNum);

    const response = await fetch('https://spfl-happy-hunting-grounds.hostless.app//players?player=' + encodeURIComponent(query));
    const names = await response.json();

    suggestionsList.innerHTML = '';

    names.forEach(name => {
        const li = document.createElement('li');
        li.textContent = name;
        li.addEventListener('mousedown', function() {
            document.getElementById('player' + roundNum).value = name;
            suggestionsList.innerHTML = '';
        });
        suggestionsList.appendChild(li);
    });
}

