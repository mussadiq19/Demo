export default function EmptyState({ icon = '✅', title, message, action }) {
  return (
    <div className="flex flex-col items-center justify-center py-16 text-center">
      <span className="text-5xl mb-4">{icon}</span>
      <h3 className="text-base font-semibold text-gray-700 mb-1">{title}</h3>
      <p className="text-sm text-gray-400 max-w-xs">{message}</p>
      {action && (
        <button
          onClick={action.onClick}
          className="mt-4 text-sm text-brand-600 hover:text-brand-700 font-medium underline underline-offset-2"
        >
          {action.label}
        </button>
      )}
    </div>
  );
}
