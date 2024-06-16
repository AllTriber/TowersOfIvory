# Towers of Ivory - A Modern Text-Based Dungeon Crawler

Welcome to Towers of Ivory, a project developed by the students of the ASD class ITN-ASD-B 2024 at HAN University of Applied Sciences. This initiative is not only a technical endeavor but also an effort to connect the nostalgic value of classic text-based dungeon crawlers like Pedit5 and ADVENT with contemporary technological advancements.

## 🌟 Project Objective

Our aim is to rekindle interest in text-based dungeon crawlers by enhancing the genre with contemporary technologies, making the game continuously engaging and playable. The project is designed to attract a solid player base and breathe new life into the genre, with a special focus on multiplayer functionalities.

## 🔍 Problem Statement

The rise of games with advanced graphics has overshadowed the text-based dungeon crawler genre. There is a lack of modern games that capture and enrich the essence of classic dungeon crawlers using today’s technology, presenting a challenge for fans of the genre. HAN University has initiated this project with the vision to create a bridge between former generations and contemporary gamers through a game that is both accessible for beginners and offers depth for seasoned aficionados.

## 🎮 Features

- **Text-Based Interface**: Play in a complete ASCII world with no graphical user interface, making for a nostalgic yet fresh gaming experience.
- **Infinite World Generation**: No two play sessions have to be identical, thanks to a seed-based dynamic world generator that expands the game environment endlessly.
- **Multiplayer Capabilities**: Enjoy robust multiplayer options with real-time interaction via a peer-to-peer network that can be hosted on a local network.
- **Intelligent Software Agents**: Program your own agents with NLP-like configurations in a custom-developed language to play on your behalf, ensuring the game remains active and engaging, even in your absence.
- **Chat System**: Engage with other players through an in-game chat system, fostering a sense of community and camaraderie.

## 🚧 Known Issues

The game is currently in full development. There are many problems that we are aware of and are working to resolve. Here are some of the biggest known issues:

### Network Issues on MacOS and Linux Systems

- **UDP Broadcast Handling**: Towers of Ivory utilizes UDP broadcast for network communication. MacOS and Linux systems are configured by default to ignore UDP packets, which limits these systems to only joining games that are hosted by others, rather than hosting games themselves.

### Game Desynchronization

- **State Synchronization**: There are instances where the game may desynchronize. This is due to the lack of a proper mechanism for synchronizing the game state across all players. Currently, the game does not check a hash of the current game state against the host's state or resend the game state to all peers when discrepancies are detected. This can lead to gameplay issues and inconsistencies.

### Issues with Seeds and Game State

- **Seed-Related Desynchronization**: Desynchronization may also occur in relation to certain game seeds, particularly when a player spawns next to an item. This issue is likely related to the current method of handling items within the game state, affecting the stability and consistency of the gameplay experience.

## 🤝 Contributing

We welcome contributions to make Towers of Ivory even better.

## 📬 Contact

For major changes, questions, or feature requests, please open an issue first to discuss what you would like to change.

## ✨ Acknowledgments

- Thanks to everyone who has contributed to this project!
- Special thanks to the fans and players who continue to support and inspire the evolution of text-based games.

---

Embark on your adventure with Towers of Ivory and redefine the legacy of text-based dungeon crawlers!
