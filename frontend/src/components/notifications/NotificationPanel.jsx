import { useNotifications } from '../../hooks/useNotifications';
import { formatDistanceToNow } from 'date-fns';

export default function NotificationPanel({ open, onClose }) {
  const { notifications, markRead, isLoading } = useNotifications();
  if (!open) return null;
  return (
    <div className="absolute right-0 top-12 w-80 bg-white rounded-2xl shadow-xl border border-surface-border z-50 overflow-hidden">
      <div className="px-5 py-4 border-b border-surface-border flex justify-between items-center">
        <h3 className="text-sm font-semibold text-gray-900">Notifications</h3>
        <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-lg">×</button>
      </div>
      <div className="max-h-96 overflow-y-auto">
        {isLoading && <p className="p-5 text-sm text-gray-400 text-center">Loading...</p>}
        {!isLoading && notifications.length === 0 && (
          <div className="p-8 text-center">
            <p className="text-3xl mb-2">✅</p>
            <p className="text-sm font-medium text-gray-600">All caught up</p>
            <p className="text-xs text-gray-400 mt-1">No new notifications</p>
          </div>
        )}
        {notifications.map((n) => (
          <div
            key={n.id}
            onClick={() => !n.read && markRead(n.id)}
            className={`px-5 py-3.5 border-b border-surface-border last:border-0 cursor-pointer hover:bg-surface-muted transition-colors ${!n.read ? 'bg-brand-50' : ''}`}
          >
            <p className="text-sm text-gray-800">{n.message}</p>
            <p className="text-xs text-gray-400 mt-1">{formatDistanceToNow(new Date(n.createdAt))} ago</p>
            {!n.read && <span className="inline-block w-2 h-2 bg-brand-500 rounded-full mt-1" />}
          </div>
        ))}
      </div>
    </div>
  );
}
