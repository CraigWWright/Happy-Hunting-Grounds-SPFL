'use strict';

const BASE_URL = 'https://spfl-happy-hunting-grounds.hostless.app';

let usedCombinations = [];
let currentLevel = 1;
let totalScore = 0;

document.addEventListener("DOMContentLoaded", function () {
    startLadder();
})

// Register guess listeners for all 10 rungs
for (let i = 1; i <= 10; i++) {
    document.getElementById("guessSelection" + i).addEventListener("submit", guessEntry);
}

function startLadder() {
    fetchLevel(currentLevel);
}

async function fetchLevel(level) {
    const used = encodeURIComponent(usedCombinations.join(","));
    const response = await fetch(BASE_URL + '/level?userSelectedLevel=' + level + '&used=' + used);
    const levelData = await response.json();

    localStorage.setItem("levelData" + level, JSON.stringify(levelData));
    usedCombinations.push(levelData.team + "|" + levelData.ground);

    document.getElementById("rungHeader" + level).classList.add('hidden');
    document.getElementById("guessSec" + level).classList.remove('hidden');
    document.getElementById("level" + level).innerHTML = levelData.team + " at " + levelData.ground;
    document.getElementById("levelLabel" + level).innerHTML = "Level " + level;
}

function guessEntry(event) {
    event.preventDefault();
    const playerSelection = document.getElementById("player" + currentLevel).value;
    const seasonSelection = document.getElementById("season" + currentLevel).value;
    fetchGuess(playerSelection, seasonSelection);
}

async function fetchGuess(playerSelection, seasonSelection) {
    const text = localStorage.getItem("levelData" + currentLevel);
    const obj = JSON.parse(text);
    const response = await fetch(BASE_URL + '/guess?team=' + encodeURIComponent(obj.team) + '&ground=' + encodeURIComponent(obj.ground) + '&season=' + encodeURIComponent(seasonSelection) + '&playerName=' + encodeURIComponent(playerSelection) + '&level=' + obj.level);
    const data = await response.json();

    // disable the submit button so they can't resubmit this rung
    document.getElementById("answerSubmit" + currentLevel).disabled = true;

    const result = document.getElementById("result" + currentLevel);
    const resultCard = document.getElementById("resultCard" + currentLevel);
    const challenge = document.getElementById("resultChallenge" + currentLevel);
    const points = document.getElementById("resultPoints" + currentLevel);
    const resultLevelLabel = document.getElementById("resultLevelLabel" + currentLevel);

    challenge.innerHTML = obj.team + " at " + obj.ground;
    document.getElementById("guessSec" + currentLevel).classList.add('hidden');
    resultCard.classList.remove('hidden');

    if (data.correct === 0) {
        // completely wrong player
        result.innerHTML = playerSelection + " did not score for " + obj.team + " at " + obj.ground + ".";
        points.innerHTML = "+0 points";
        resultLevelLabel.innerHTML = "Level " + currentLevel;
        endLadder();

    } else if (data.correct === 1) {
        // right player, wrong season
        result.innerHTML = playerSelection + " has scored there but not in the " + seasonSelection + " season.";
        points.innerHTML = "+0 points";
        resultLevelLabel.innerHTML = "Level " + currentLevel;
        endLadder();

    } else if (data.correct === 2) {
        // correct
        totalScore += data.points;
        result.innerHTML = "Correct! " + data.homeTeam + " " + data.finalScore + " " + data.awayTeam + " (" + data.date + ")";
        points.innerHTML = "+" + data.points + " points";
        resultLevelLabel.innerHTML = "Level " + currentLevel;

        document.getElementById("totalScoreDisplay").innerHTML = "Total score: " + totalScore;

        if (currentLevel < 2) {
            currentLevel++;
            fetchLevel(currentLevel);
        } else {
            document.getElementById("completedMsg").classList.remove('hidden');
            endLadder();
        }
    }
}

function endLadder() {
    document.getElementById("finalScore").classList.remove('hidden');
    document.getElementById("totalScoreDisplay").innerHTML = "Total score: " + totalScore;
    document.getElementById("ladderStartBtn").disabled = false;
    document.getElementById("ladderStartBtn").value = "Play Again";
}

let debounceTimer = null;

function playerEntry(event) {
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
    const response = await fetch(BASE_URL + '/players?player=' + encodeURIComponent(query));
    const names = await response.json();

    suggestionsList.innerHTML = '';

    names.forEach(name => {
        const li = document.createElement('li');
        li.textContent = name;
        li.addEventListener('mousedown', function () {
            document.getElementById('player' + roundNum).value = name;
            suggestionsList.innerHTML = '';
        });
        suggestionsList.appendChild(li);
    });
}
